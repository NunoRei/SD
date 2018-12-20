import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author João Marques, Nuno Rei e Jaime Leite
 */

public class Catalogo{
    private static class servidor{
        private String id;
        private String type;
        private Double preco;
        private int estado;
        Lock ls = new ReentrantLock();

        //estado 1 a ser usado e 0 a nao ser usado
        private servidor(String id, String type, Double preco, int estado){
            this.id = id;
            this.type = type;
            this.preco = preco;
            this.estado = estado;
        }

        public String getId(){
            return this.id;
        }

        public String getType(){
            return this.type;
        }

        public Double getPreco(){
            return preco;
        }

        public int getEstado(){
            return estado;
        }

        public void setEstado(int i){
            this.estado = i;
        }
    }

    private ArrayList<servidor> servidores = new ArrayList<>();
    //private ArrayList<servidor> pedido = new ArrayList<>();
    private Lock l = new ReentrantLock();

    //0 se esta para ser reservado a pedido; 1 se esta a ser usado; 2 se esta para ser reservado em leilao
    public Catalogo(){
        servidores.add(new servidor("t3.micro","micro",0.99,0));
        servidores.add(new servidor("t3.large","large",1.50,0));
        servidores.add(new servidor("m5.micro","micro",0.50,2));
        servidores.add(new servidor("m5.large","large",1.00,2));
    }

    //retorna uma posicao do array que esteja livre
    public int existeServerType(String type){
        int i;
        for(i=0; i<this.servidores.size(); i++){
            if(this.servidores.get(i).getType().equals(i)) return i;
        }
        return -1;
    }

    public int serverPedidoFree(){
        int i;
        for(i=0; i<this.servidores.size(); i++)
            if(servidores.get(i).getEstado() == 0) return 0;
        return 1;
    }

    public int serverLeilaoFree(){
        int i;
        for(i=0; i<this.servidores.size(); i++)
            if(servidores.get(i).getEstado() == 2) return 0;
        return 1;
    }

    public ArrayList<servidor> getCatalogo(){
        return this.servidores;
    }

    public void lock(){
        this.ls.lock();
    }

    public void unlock(){
        this.ls.unlock();
    }
}
