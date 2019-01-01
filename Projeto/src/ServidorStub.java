import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author João Marques, Nuno Rei, Jaime Leite e Hugo Nogueira
 */

public class ServidorStub implements interfaceGlobal{

    //map que contem clientes que fazem parte do sistema
    private Map<String, Cliente> clientes = new HashMap<>();
    private final Lock lClientesAtivos = new ReentrantLock();
    // Clientes que se encontram ativos no sistema
    public Map<String, Socket> clientesativos = new HashMap<>();
    private final Lock lClientesLeilao = new ReentrantLock();
    //fila diferente da seguinte da anterior, pois aqui os clientes so entram quando estao a participar num leilao
    //clientes conectados e que pretendem obtrer servidor em leilao
    private Map<String, Double> clientesLeilao = new HashMap<>();
    private Catalogo cat = new Catalogo();

    private String clienteAtual;
    private float pricePerHour;

    private static class Cliente {
        private String email;
        private String password;
        private String idservidor; // string vazia se nao tiver nenhum
        private double divida;

        public Cliente(String email,String pass) {
            this.email = email;
            this.password = pass;
            this.idservidor = "";
            this.divida = 0;
        }

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }

        public double getDivida(){
            return this.divida;
        }
        
        public void setDivida(double divida){
            this.divida = divida;
        }

        public void setIdservidor(String id) {
            this.idservidor = id;
        }

        public String getIdservidor() {
            return this.idservidor;
        }
    }

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

    @Override
    public String reservarPorPedido(String email, String type){
        String resultado = null;
        try {
            resultado = cat.reservaPedido(type);
            if (!resultado.equals(""))
                clientes.get(email).setIdservidor(resultado);
            else resultado = "Servidor inexistente";
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    @Override
    public String libertaReserva(String email, String id){
        String resultado = "";
        try {
            //libertar um servidor do map cat.pedido
            if (clientes.get(email).getIdservidor().equals(id) && !id.equals("t4")){
                cat.libertaReserva(id);
                clientes.get(email).setIdservidor("");
                double precoServer = cat.getPrice(id);
                resultado += precoServer;
            }
            //libertar um servidor do map cat.leilao
            if (clientes.get(email).getIdservidor().equals(id) && id.equals("t4")){
                cat.libertaReserva(id);
                clientes.get(email).setIdservidor("");
                double precoServer = cat.getPrice(id);
                resultado += precoServer;   
                //se nao estiver vazio, atribui o servidor ao que cliente que tiver dado maior oferta
                if(this.clientesLeilao.isEmpty() == false){
                    double maiorOferta = 0;
                    String clienteMaiorOferta = "";
                    for (Map.Entry<String, Double> cliente : this.clientesLeilao.entrySet()){
                	if (cliente.getValue() > maiorOferta) {
                            maiorOferta = cliente.getValue();
                            clienteMaiorOferta = cliente.getKey();
                	}
                    }
	            clientes.get(clienteMaiorOferta).setIdservidor(id);
	            PrintWriter skout = new PrintWriter(this.clientesativos.get(clienteMaiorOferta).getOutputStream());
	            skout.println("Obteve o servidor que pretendia");
	            skout.flush();
                    this.clientesLeilao.clear();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void setValorDivida(String email, double divida){
        this.clientes.get(email).setDivida(divida);
    }
    
    public double getValorDivida(String email){
        return this.clientes.get(email).getDivida();
    }

    public String leilao(String email, String preco, String type){
        String resultado = null;
        try {
            resultado = cat.reservaLeilao(type);
            //se houver servidores livres, coloca logo lá o cliente
            if (!resultado.equals("Indisponível"))
                clientes.get(email).setIdservidor(resultado);
            //se nao houver servidores livres, nao coloca lá o cliente e comeca o leilao
            else{
            	//se map clientesLeilao estiver vazio, significa que o cliente pode comecar leilao, senao vai incluir-se em leilao
            	if(this.clientesLeilao.isEmpty() == true){
                    this.clientesLeilao.put(email, Double.parseDouble(preco));
	            for (Map.Entry<String, Socket> cliente : this.clientesativos.entrySet()){
	                //aqui verificar se o nome dos clientesLeilao nao e igual ao email
                        if (!cliente.getKey().equals(email)) {
                            PrintWriter skout = new PrintWriter(cliente.getValue().getOutputStream());
	                    skout.println("all: Leilao iniciado");
	                    skout.flush();
	                }
	            }
            	}
                else {
                    lClientesLeilao.lock();
                }
            	this.clientesLeilao.put(email, Double.parseDouble(preco));
            	lClientesLeilao.unlock();
            }
        }
        catch (IOException | InterruptedException | NumberFormatException e) {
            e.printStackTrace();
        }
        //se nao houver servidores para leilao livres, comeca o leilao
        return resultado;
    }

    //cliente quer sair, usando um exit
    // retorna a posicao do servidor que libertou
    public int retiraServidorExit(String email) {
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