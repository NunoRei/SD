import java.util.HashMap;
import java.util.Map;
//import Cliente.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author João Marques, Nuno Rei e Jaime Leite
 */

public class Servidor implements Runnable{
    //private Servidor[] servidores = new Servidor[30]; //ports [1200;1229]
    //private int[] ocupados = new int[30]; //0-> livre, 1-> ocupado, 2-> leilão
    private final Socket x;
    private ServidorStub st;
    private Map<String, Socket> clientesConectados = new HashMap<>();

    public Servidor(Socket x, ServidorStub st){
        this.x = x;
        this.st = st;
    }

    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(12345);
        ServidorStub st = new ServidorStub();
        while (true) {
            Socket x = ss.accept();
            new Thread(new Servidor(x,st)).start();
        }
    }

    public void run(){
        try {
            PrintWriter out = new PrintWriter(x.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(x.getInputStream()));
            while (true){
                String s = in.readLine();
                String[] p = s.split(" ");
                switch(p[0]){
                    case "regista":
                        int resregista = st.registaCliente(p[1], p[2]);
                        s = Integer.toString(resregista);
                        break;
                    case "autentica":
                        int resautentica = st.autenticaCliente(p[1], p[2]);
                        s = Integer.toString(resautentica);
                        if(resautentica == 0)
                            this.clientesConectados.put(p[0],x);
                        break;
                        
                    case "servidor_Pedido":
                        //caso esteja conectado
                        if(this.clientesConectados.containsKey(p[1])){
                            int resservPedido = st.reservarPorPedido(p[1], p[2]);
                            s = Integer.toString(resservPedido);
                        }
                        break;

                    //para aqui tem de indicar o nickname de utilizador, para ver se ja esta autenticado, o preço horário e indicar o tipo de servidor que quer reservar
                    case "servidor_Leilao":
                        if(this.clientesConectados.containsKey(p[1])){
                        	double value = Double.parseDouble(p[2]);
                            int resservLeilao = st.reservarPorLeilao(p[1], value, p[3]);
                            s = Integer.toString(resservLeilao);
                        }
                        break;
                    
                    //caso em que o cliente escreve exit para sair so sistema
                    case "exit":
                        if(this.clientesConectados.containsKey(p[1])){
                            int pretendeSairExit = st.retiraServidorExit(p[1]);
                            s = Integer.toString(pretendeSairExit);
                        }
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
                //if (s.equals("3")) break;
                out.println(s);
                out.flush();
                //a parte de remover o cliente tem de ser aqui, senao perde-se o socket e nao se consegue fazer o println e o flush
                if(p[0].equals("exit") || (s == null)) this.clientesConectados.remove(p[1]);
            }
        }
            //out.close();
            //x.close();
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
