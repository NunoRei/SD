import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * @author João Marques, Nuno Rei, Jaime Leite e Hugo Nogueira
 * @version 01-2019
 */
public class ServidorImpl implements interfaceGlobal{
    //map que contem clientes que fazem parte do sistema
    private Map<String, Cliente> clientes = new HashMap<>();
    // Clientes que se encontram ativos no sistema
    private final Lock lClientesAtivos = new ReentrantLock();
    // Clientes que se encontram ativos no sistema
    public Map<String, Socket> clientesativos = new HashMap<>();
    private final Catalogo cat = new Catalogo();

    private String clienteAtual;
    private float pricePerHour;

    private static class Cliente {
        private final String email;
        private final String password;
        private String idservidor; // string vazia se nao tiver nenhum
        private double divida;
        private final Lock lc = new ReentrantLock();

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
    public synchronized int registaCliente(String email, String pass) {
        Cliente c = this.clientes.get(email);
        if (c != null) return 1;
        else {
            c = new Cliente(email,pass);
            this.clientes.put(email,c);
            return 0;
        }
    }

    @Override
    public int autenticaCliente(String email, String pass) {
        lClientesAtivos.lock();
        try {
            Cliente c = clientes.get(email);
            if (c == null) {
                lClientesAtivos.unlock();
                return 1;
            }
            c.lc.lock();
            try {
                lClientesAtivos.unlock();
                if (clientesativos.containsKey(email)) return 3;
                if (c.getPassword().equals(pass)) return 0;
                return 2;
            }
            finally {
                c.lc.unlock();
            }
        }
        catch (Exception e) {return 1;}
    }

    @Override
    public String reservarPorPedido(String email, String type){
        String resultado;
        resultado = cat.reservaPedido(type);
        if (!resultado.equals("")) {
            clientes.get(email).setIdservidor(resultado);
            resultado = "id para libertar: " + resultado;
        }
        else resultado = "Servidor inexistente";
        return resultado;
    }

    @Override
    public String reservarLeilao(String email, String type, String valor) {
        String resultado = cat.reservaLeilao(email,type,Double.parseDouble(valor));
        if (!resultado.equals("")) {
            clientes.get(email).setIdservidor(resultado);
            resultado = "id para libertar: " + resultado;
        }
        else resultado = "Servidor inexistente";
        return resultado;
    }

    @Override
    public String libertaReserva(String email, String id){
        String resultado = "";
        if (clientes.get(email).getIdservidor().equals(id)) {
            if (id.contains("l")) cat.libertaReservaLeilao(id);
            else cat.libertaReservaPedido(id);
            clientes.get(email).setIdservidor("");
            double precoServer = cat.getPrice(id);
            resultado = Double.toString(precoServer);
        }
        return resultado;
    }

    public synchronized void setValorDivida(String email, double divida){
        double dividaAcumulada = getValorDivida(email);
        this.clientes.get(email).setDivida(dividaAcumulada + divida);
    }

    public synchronized double getValorDivida(String email){
        return this.clientes.get(email).getDivida();
    }

    //retorna o preco a que o cliente reservou o server que possui, caso possua algum
    public synchronized double temServidor(String email){
        Cliente cliente = this.clientes.get(email);
        if(cliente != null && !cliente.getIdservidor().equals(""))
            return this.cat.getPrice(cliente.getIdservidor());
        return 0;
    }

    public synchronized void logOut(String email) {
        Cliente cliente = this.clientes.get(email);
        if(cliente != null) {
            if (!cliente.getIdservidor().equals("")) {
                libertaReserva(email,cliente.getIdservidor());
            }
            this.clientesativos.remove(email);
        }
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

    //Atualizar preco por hora em caso de leilao
    public void setPricePerHour(float price) {
        this.pricePerHour = price;
    }
}