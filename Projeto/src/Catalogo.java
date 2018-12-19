import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author João Marques, Nuno Rei e Jaime Leite
 */

public class Catalogo {

    private static class servidor {
        private String id;
        private String type;
        private Double preco;
        private int estado;
        Lock ls = new ReentrantLock();

        //estado 1 a ser usado e 0 a nao ser usado
        private servidor(String id, String type, Double preco, int estado) {
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

        public int getEstado(){
            return estado;
        }
    }

    private ArrayList<servidor> servidores = new ArrayList<>();
    //private ArrayList<servidor> pedido = new ArrayList<>();
    private Lock l = new ReentrantLock();

    public Catalogo() {
        servidores.add(new servidor("t3.micro","micro",0.99,0));
        servidores.add(new servidor("t3.large","large",1.50,0));
        servidores.add(new servidor("m5.micro","micro",0.50,0));
        servidores.add(new servidor("m5.large","large",1.00,0));
    }

    public ArrayList<servidor> getCatalogo(){
        return this.servidores;
    }
}
