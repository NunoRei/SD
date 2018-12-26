import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.HashMap;

public class Servidor extends Thread{
	private final Socket x;
    private ServidorStub st;
    private Map<String, Socket> clientesConectados;

    public Servidor(Socket x, ServidorStub st, Map<String, Socket> clientesConectados){
        this.x = x;
        this.st = st;
        this.clientesConectados = clientesConectados;
    }

    public void run(){
    	String info = null, nickname = null, name = null;
    	int feedBack = 0, resautentica, resregista;
    	PrintWriter out, outClienteConectado;
    	BufferedReader in,inClienteConectado;
    	
        try {
            out = new PrintWriter(x.getOutputStream());
            in = new BufferedReader(new InputStreamReader(x.getInputStream()));
            
            //fase de negociacao do cliente com o servidor para se registar ou autenticar no sistema
            while(feedBack == 0){
				//inClientePedido = new BufferedReader(new InputStreamReader(this.clSock.getInputStream()));
				out.println("Insira o nome e passe de utilizador");
				out.flush();

				info = in.readLine();
				String[] e = info.split(" ");
				
				if((resautentica = st.autenticaCliente(e[0], e[1])) == 0) {
					out.println("Está autenticado no sistema");
					out.flush();
					
					feedBack = 1;
					this.clientesConectados.put(e[0],x);
		            //guarda o nickname para ser aplicado ao proximo while
					nickname = e[0];
				};
				
				if( (resregista = st.registaCliente(e[0], e[1])) == 0) {
					out.println("Está registado no sistema");
					out.flush();
					
					feedBack = 1;
					this.clientesConectados.put(e[0],x);
		            //guarda o nickname para ser aplicado ao proximo while
					nickname = e[0];
				}
			}
            
            //depois de o cliente estar autenticado ou registado, o servidor recebe qualquer comando que venha do cliente
            while (true){
                String s = in.readLine();
                String[] p = s.split(" ");
                
                out.println("Insira a operacao que pretende fazer");
				out.flush();
				
                switch(p[0]){
                    case "servidor_Pedido":
                    		//p[1] é o type que o cliente indica
                            int resservPedido = st.reservarPorPedido(nickname,p[1],x);
                            if(resservPedido <= 0) s = "Foi inserido em fila de espera";
                            else{
                            	s = "O servidor pretendido foi-lhe atribuído na posição ";
                            	s += Integer.toString(resservPedido) ;
                            }
                        break;

                    //para aqui tem de indicar o nickname de utilizador, para ver se ja esta autenticado, o preço horário e indicar o tipo de servidor que quer reservar
                    case "servidor_Leilao":
                        	double value = Double.parseDouble(p[2]);
                        	//type é o p[1] e preço é o p[2]
                            int resservLeilao = st.reservarPorLeilao(nickname,value,p[1]);
                            s = Integer.toString(resservLeilao);
                        break;
                    
                    //caso em que o cliente escreve exit para sair so sistema
                    //o servidor informa todos os clientes que passa a haver mais um servidor disponivel, dizendo a posicao do mesmo
                    case "exit":
                            int pretendeSairExit = st.retiraServidorExit(nickname);
                            
                            //informa toda a gente acerca de servidor disponível
                            if(pretendeSairExit < 0) s = "Não conseguiu fazer exit no sistema";
                            else
                            	for(Map.Entry<String,Socket> e : this.clientesConectados.entrySet()){
									name = e.getKey();
									if(!name.equals(nickname)){
										inClienteConectado = new BufferedReader(new InputStreamReader(e.getValue().getInputStream()));
										outClienteConectado = new PrintWriter(e.getValue().getOutputStream());
										outClienteConectado.println("O cliente " + nickname + "saiu do sistema e deixou livre o servidor " + pretendeSairExit);
										outClienteConectado.flush();
									}
                            	}
                            	s = "Conseguiu sair do sistema";
                        break;

                    default:
                    	//se if entao é porque n passou nada para o servidor senao é pq passou um comando inválido
                    	if(s == null){
                    		//variavel nickname reulta de ir ao map de clientes conectados e ficar com a key do socket em questao
                            //String resservLeilao = st.retiraServidor(nickname);
                            //s = resservLeilao;
                    	}
                    	else System.out.println("Comando invalido.");
                }
                
                out.println(s);
                out.flush();
                
                //a parte de remover o cliente tem de ser aqui, senao perde-se o socket e nao se consegue fazer o println e o flush
                if(p[0].equals("exit") || (s == null)) this.clientesConectados.remove(nickname);
            }
        }
            //out.close();
            //x.close();
        catch (Exception e){
            e.printStackTrace();
        }
    }
}


