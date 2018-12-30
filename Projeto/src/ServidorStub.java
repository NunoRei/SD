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

    private String clienteAtual;
    private float pricePerHour;

    private static class Cliente {
        private String email;
        private String password;
        private double divida;

        public Cliente(String email,String pass){
            this.email = email;
            this.password = pass;
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

    public String reservarPorPedido(String type){
        String resultado = null;
        try {
            resultado = cat.reservaPedido(type);
        }
        catch (Exception e) {

        }
        return resultado;
    }

    public String libertaReserva(String id){
        String resultado = "";
        try {
            cat.libertaReserva(id);
            double precoServer = cat.getPrice(id);
            resultado += precoServer;
        }
        catch (Exception e) {

        }
        return resultado;
    }
    
    public void setValorDivida(String email, double divida){
        this.clientes.get(email).setDivida(divida);
    }
    
    public double getValorDivida(String email){
        return this.clientes.get(email).getDivida();
    }
    
    /*metodo chamado quando um cliente quer iniciar ou entrar num leilao por um servidor
      retorna 0 se cliente consegue iniciar ou entrar num leilao
      retorna 1 se n houver servidores daquele tipo disponiveis para leilao*/
    /*public int reservarPorLeilao(String email, double preco, String type){
        if(this.cat.existeServer(type) < 0){
        //o cliente vai a leilao para tentar ficar com o servidor
    }
       
        return 0;
    }*/

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