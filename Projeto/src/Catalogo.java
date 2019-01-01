import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Catalogo {
    private final int MAX = 2;
    private Map<String,Servidor> pedido;
    private int ocupacaopedido = 0;
    private Map<String,Servidor> leilao;
    private int ocupacaoleilao = 0;
    private final Lock l = new ReentrantLock();
    private final Condition pavaliable = l.newCondition();
    private final Condition lavaliable = l.newCondition();

    private class Servidor {
        private final String id;
	private final String type;
	private Double preco;
	private int quantidade;
	private final Condition notTaken = l.newCondition();

	public Servidor(String id, String type, Double preco, int quantidade){
	    this.id = id;
	    this.type = type;
	    this.preco = preco;
	    this.quantidade = quantidade;
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

    public Catalogo(){
	this.pedido = new HashMap<>();
	this.leilao = new HashMap<>();
	pedido.put("micro",new Servidor("t1","micro",0.99,2));
	pedido.put("medium",new Servidor("t2","medium",1.50,2));
	pedido.put("large",new Servidor("t3","large",1.00,2));
	leilao.put("micro",new Servidor("t4","micro",0.99,2));
    }
	
    public double getPrice(String id) {
	return this.pedido.get(id).getPreco();
    }
        /*//retorna > 0 se há servidores daquele tipo livres e 0 caso contrário
	    public int existeServer(String type){
	        int i;
	        for(i=0; i<this.servidores.size(); i++){
	            if(this.servidores.get(i).getType().equals(type) && this.servidores.get(i).getQuantidade() > 0) return 1;
	        }
	        return 0;
	    }*/

    public String reservaPedido(String type) throws InterruptedException {
	l.lock();
	try {
            /*while(ocupacaopedido == MAX) {
		pavaliable.await();
            }
            ocupacaopedido+=1;*/
            Servidor s = this.pedido.get(type);
            if (s != null) {
		while (s.quantidade == 0) {
                    s.notTaken.await();
                }
		s.quantidade -= 1;
		return s.type;
            }
            return "";
	}
	finally {
            l.unlock();
	}
    }
	
    public String reservaLeilao(String type) throws InterruptedException {
	l.lock();
	try {
            /*while(ocupacaopedido == MAX) {
		pavaliable.await();
            }
            ocupacaopedido+=1;*/
            Servidor s = this.leilao.get(type);
            if (s != null){
		if (s.quantidade == 0){
                    return "Indispon�vel";
		}
		s.quantidade -= 1;
		return s.type;
            }
            return "Indispon�vel";
	}
	finally {
            l.unlock();
	}
    }

    public void libertaReserva(String id){
	l.lock();
	try {
            if(!id.equals("t4")){
		Servidor s = this.pedido.get(id);
		s.quantidade += 1;
		s.notTaken.signalAll();
            }
            else {
		Servidor s = this.leilao.get(id);
		s.quantidade += 1;
            }
	}
	finally {
            l.unlock();
	}
    }
}