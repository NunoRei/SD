import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author João Marques, Nuno Rei, Jaime Leite e Hugo Nogueira
 */

public class ServidorStub implements interfaceGlobal{
    //map que contem clientes que fazem parte do sistema
    private Map<String, Cliente> clientes = new HashMap<>();
    /* Clientes que se encontram ativos no sistema */
    public Map<String, Socket> clientesativos = new HashMap<>();
    //clientes conectados e à espera, que pretendem obter servidor por pedido
    private Map<String, Socket> clientesPedido = new HashMap<>();
    //fila diferente da seguinte da anterior, pois aqui os clientes so entram quando estao a participar num leilao
    //clientes conectados e que pretendem obtrer servidor em leilao
    private Map<String, Socket> clientesLeilao = new HashMap<>();
    private Catalogo cat = new Catalogo();
    
    private String serverName;
    private String serverType;
    
    private static ServerSocket SS;
    private String clienteAtual;
    private float pricePerHour;
    
    private BufferedReader in;
    private PrintWriter out;

    private static class Cliente {
        private String email;
        private String password;
        private float value_to_pay;
        private int conectado;

        public Cliente(String email,String pass){
            this.email = email;
            this.password = pass;
            this.value_to_pay = 0;
            //this.conectado = 0;
        }

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }

        public float getValue_to_pay() {
            return value_to_pay;
        }
    }
    /*
    //Construtor de um Servidor
    public ServidorStub(String nome, String tipo, float price) {
        this.serverName = nome;
        this.serverType = tipo;
        this.pricePerHour = price;
    }*/

    @Override
    public int registaCliente(String email, String pass) {
        Cliente c;
        if ((c = clientes.get(email)) != null) return 1;
        else {
            c = new Cliente(email,pass);
            clientes.put(email,c);
            return 0;
        }
    }

    @Override
    public int autenticaCliente(String email, String pass) {
        Cliente c;
        if ((c = clientes.get(email)) == null) return 1;
        else if ((c.getPassword().equals(pass)) && !clientesativos.containsKey(email)) return 0;
            else return 1;
    }

    //tipo: 0 ou 2
    //retorna >=0 se fica com servidor pretendido e -1 caso vá para fila de espera
    //se nao consegue reservar, vai para fila de espera até que chegue a sua vez de obter um servidor
    public int reservarPorPedido(String email, String type, Socket x){            
            int resultado;
    		//se nao existe servidor que cliente pretende, cliente vai para fila de espera de clientes na mesma situacao(reservar servidor a pedido)
            if(this.cat.existeServer(type) < 0){
            		//await do cliente
            }

            else{
                //indica no cliente a posicao no array do servidor que lhe foi atribuido
                this.clientes.get(email).setServidor(resultado);
                
                //nao sei se este metodo está bem, mas faz sentido avisar toda a gente que está conectada quando se atrui um servidor 
                //atribuirServidor(email);
                
                this.cat.lock();
	            //vai passar a haver menos uma quantidade daquele tipo de servidor    
		    this.cat.decrementQuantidade(type);
                this.cat.unlock();
            }
            
            return resultado;
    }

    /*metodo chamado quando um cliente quer iniciar ou entrar num leilao por um servidor
      retorna 0 se cliente consegue iniciar ou entrar num leilao
      retorna 1 se n houver servidores daquele tipo disponiveis para leilao*/
    public int reservarPorLeilao(String email, double preco, String type){
    	if(this.cat.existeServer(type) < 0){
		//o cliente vai a leilao para tentar ficar com o servidor
	}
       
        return 0;
    }

    //cliente quer sair, usando um exit
    // retorna a posicao do servidor que libertou
    public int retiraServidorExit(String email){
           /*
	   quando o cliente sai do sistema, avisa os outros que pretendem obter um servidor do tipo que ele tem, ou seja,
	   faz um notify, ou para os que reservaram a pedido, para aqueles que estão em leilão 	
	    */
           
           return 0;
    }

    //Colocar o cliente no servidor
    public void addCliente(String nome) {
        this.clienteAtual = nome;
    }
    
    //Retirar o cliente do servidor
    public void removeCliente() {
        this.clienteAtual = null;
    }
    
    //Retornar nome do cliente
    public String getClienteAtual() {
        return this.clienteAtual;
    }
    
    //Atualizar preço por hora em caso de leilão
    public void setPricePerHour(float price) {
        this.pricePerHour = price;
    }
    
    //Enquanto o cliente estiver a escrever para o servidor
    //Falta colocar Times para calcular o tempo que o cliente esteve no servidor para adicionar há sua dívida
    /*public float init(int port) throws IOException {
        SS = new ServerSocket(port);
        Socket x;
        //Time timeInicio = Time.now();
        while (true) {
            x = SS.accept();
            PrintWriter out = new PrintWriter(x.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(x.getInputStream()));
            while (true) {
                String s = in.readLine();
                if(s.equals("exit")) break;
                //falta por aqui, penso eu, o caso do ele dizer que quer abandonar o servidor que obteve
                out.println(s);
                out.flush();
            }
            out.close();
            x.close();
            removeCliente();
            break;
        }
        //Time timeFim = Time.now();
        float price = 0;//(toHours(timeFim) - toHours(timeInicio))*pricePerHour;
        return price;
    }
    */
}
