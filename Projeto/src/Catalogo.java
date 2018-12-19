import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Catalogo {

    private static class servidor {
        private String id;
        private String type;
        private Double preco;
        Lock ls = new ReentrantLock();

        private servidor(String id, String type, Double preco) {
            this.id = id;
            this.type = type;
            this.preco = preco;
        }

        public String getId() {
            return this.id;
        }

        public String getType() {
            return this.type;
        }

        public Double getPreco() {
            return preco;
        }
    }

    private ArrayList<servidor> servidores = new ArrayList<>();
    //private ArrayList<servidor> pedido = new ArrayList<>();
    private Lock l = new ReentrantLock();

    public Catalogo() {
        servidores.add(new servidor("t3.micro","micro",0.99));
        servidores.add(new servidor("t3.large","large",1.50));
        servidores.add(new servidor("m5.micro","micro",0.50));
        servidores.add(new servidor("m5.large","large",1.00));
    }

}

