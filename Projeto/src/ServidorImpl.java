//import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.*;
/**
 *
 * @author João Marques, Nuno Rei, Jaime Leite e Hugo Nogueira
 * @version 01-2019
 */

public class ServidorImpl implements interfaceGlobal{
    //map que contem clientes que fazem parte do sistema
    private Map<String, Cliente> clientes = new HashMap<>();
    /* Clientes que se encontram ativos no sistema */
    private final Lock lClientesAtivos = new ReentrantLock();
    // Clientes que se encontram ativos no sistema
    public Map<String, Socket> clientesativos = new HashMap<>();
    //clientes conectados e � espera, que pretendem obter servidor por pedido
    private Map<String, Socket> clientesPedido = new HashMap<>();
    //fila diferente da seguinte da anterior, pois aqui os clientes so entram quando estao a participar num leilao
    //clientes conectados e que pretendem obtrer servidor em leilao
    private Map<String, Socket> clientesLeilao = new HashMap<>();
    private Catalogo cat = new Catalogo();

    private String clienteAtual;
    private float pricePerHour;

    private static class Cliente {
        private final String email;
        private final String password;
        private String idservidor; // string vazia se nao tiver nenhum
        private double divida;

        public Cliente(String email,String pass) {
            this.email = email;
            this.password = pass;
            this.idservidor = "";
            this.divida = 0.0;
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
            if (!resultado.equals("")) {
                clientes.get(email).setIdservidor(resultado);
                resultado = "id para libertar: " + resultado;
            }
            else resultado = "Servidor inexistente";
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public String reservarLeilao(String email, String type, String valor) {
        String resultado = null;
        try {
            resultado = cat.reservaLeilao(email,type,Double.parseDouble(valor));
            if (!resultado.equals("")) {
                clientes.get(email).setIdservidor(resultado);
                resultado = "id para libertar: " + resultado;
            }
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
        if (clientes.get(email).getIdservidor().equals(id)) {
            String[] p = id.split(" ");
            if (id.contains("l")) {
                cat.libertaReservaLeilao(id);
                clientes.get(email).setIdservidor("");
                double precoServer = cat.getPrice(id);
                resultado += precoServer;
            }
            else {
                cat.libertaReservaPedido(id);
                clientes.get(email).setIdservidor("");
                double precoServer = cat.getPrice(id);
                resultado += precoServer;
            }
        }
        return resultado;
    }

    public void setValorDivida(String email, double divida){
        double dividaAcumulada = this.getValorDivida(email);
        this.clientes.get(email).setDivida(dividaAcumulada + divida);
    }

    public double getValorDivida(String email){
        return this.clientes.get(email).getDivida();
    }

    //retorna o preco a que o cliente reservou o server que possui, caso possua algum
    public double temServidor(String email){
        //vejo se cliente tem ou não servidor
        if(!this.clientes.get(email).getIdservidor().equals("")){
            return this.cat.getPrice(this.clientes.get(email).getIdservidor());
        }

        return 0;
    }

    public void logOut(String email) {
        Cliente cliente = this.clientes.get(email);
        if (!cliente.getIdservidor().equals("")) {
            libertaReserva(email,cliente.getIdservidor());
        }
        this.clientesativos.remove(email);
    }

    //cliente quer sair, usando um exit
    // retorna a posicao do servidor que libertou
    public int retiraServidorExit(String email) {
           /*
       quando o cliente sai do sistema, avisa os outros que pretendem obter um servidor do tipo que ele tem, ou seja,
       faz um notify, ou para os que reservaram a pedido, para aqueles que est�o em leil�o
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

    //Atualizar pre�o por hora em caso de leil�o
    public void setPricePerHour(float price) {
        this.pricePerHour = price;
    }

    //Enquanto o cliente estiver a escrever para o servidor
    //Falta colocar Times para calcular o tempo que o cliente esteve no servidor para adicionar h� sua d�vida
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