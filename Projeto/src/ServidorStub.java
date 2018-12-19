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
 * @author João Marques, Nuno Rei e Jaime Leite
 */

public class ServidorStub implements interfaceGlobal {
    private Map<String, Cliente> clientes = new HashMap<>();
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
            this.conectado = 0;
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

        public int getEstado(){
            return this.conectado;
        }

        public void setEstado(int estado){
            this.conectado = estado;
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
        else if (c.getPassword().equals(pass)) return 0;
            else return 1;
        this.clientes.get(c).setConectado(1);
    }
    
    public String reservarPorPedido(String email, String tipo){
        if(this.clientes.get(c).getEstado() == 1){
            
        }
    }

    public String reservarPorLeilao(String email, double preco, String tipo){
        if(this.clientes.get(c).getEstado() == 1){
            
        }
    }

    public void atribuirServidor(String email){
        //this.users.lock();

        for(Map.Entry<String,Socket> c : this.clientesConectados.entrySet()){
                    name = c.getKey();
           
                    if(!name.equals(email)){
                        in = new BufferedReader(new InputStreamReader(c.getValue().getInputStream()));
                        out = new PrintWriter(c.getValue().getOutputStream());
                        out.println("O servidor na posicao" + "foi atribuído a: " + email);
                        out.flush();
                    }
                        else{
                            in = new BufferedReader(new InputStreamReader(c.getValue().getInputStream()));
                            out = new PrintWriter(c.getValue().getOutputStream());
                            out.println("Foi-lhe atribuído o servidor que pretendia");
                            out.flush();
                        }
                    }
        //this.users.unlock();
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
    public float init(int port) throws IOException {
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
}
