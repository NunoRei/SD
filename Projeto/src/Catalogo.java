import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Catalogo{
	private static class Servidor {
		private String type;
		private Double preco;
		private int quantidade;
		private ReentrantLock lock;
		private Condition isFree;

		public Servidor(String type, Double preco, int quantidade){
		    //this.id = id;
		    this.type = type;
		    this.preco = preco;
		    this.quantidade = quantidade;
		    isFree = lock.newCondition();
		}
		
		public String getType(){
		    return this.type;
		}

		public Double getPreco(){
		    return this.preco;
		}

		public int getQuantidade(){
		    return this.quantidade;
		}
	}

	    private Map<String,Servidor> servidores = new HashMap<>();
	    private ReentrantLock lockservers = new ReentrantLock();

	    public Catalogo(){
	    	servidores.put("micro",new Servidor("micro",0.99,3));
	        servidores.put("medium",new Servidor("medium",1.50,4));
	        servidores.put("large",new Servidor("large",1.00,5));
	    }

	    //retorna > 0 se há servidores daquele tipo livres e 0 caso contrário
	    public int existeServer(String type){
	        int i;
	        for(i=0; i<this.servidores.size(); i++){
	            if(this.servidores.get(i).getType().equals(type) && this.servidores.get(i).getQuantidade() > 0) return 1;
	        }
	        return 0;
	    }

	    public Map<String,Servidor> getCatalogo(){
	        return this.servidores;
	    }
	
	    public void lock(){
	        this.lockservers.lock();
	    }

	    public void unlock(){
	        this.lockservers.unlock();
	    }
}
