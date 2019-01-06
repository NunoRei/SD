import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

/**
 *
 * @author João Marques, Nuno Rei, Jaime Leite e Hugo Nogueira
 * @version 01-2019
 */
public class Catalogo {
    private final int MAX = 2;
    private Map<String, Servidor> pedido;
    private Map<String, ServidorLeilao> leilao;
    private Map<String, Double> propostas;
    private int ocupacaoleilao = 0;
    private int ocupacaopropostas = 0;
    private final Lock l = new ReentrantLock();
    private final Condition pavaliable = l.newCondition();
    private final Condition notAvaliable = l.newCondition();

    private class Servidor {
	private final String id;
	private final String type;
	private final Double preco;
	private int quantidade;
	private final Lock ls = new ReentrantLock();
	private final Condition notTaken = ls.newCondition();

	public Servidor(String id, String type, Double preco, int quantidade) {
            this.id = id;
            this.type = type;
            this.preco = preco;
            this.quantidade = quantidade;
        }

	public String getType() {
            return this.type;
	}

        public Double getPreco() {
            return this.preco;
	}

	public int getQuantidade() {
            return this.quantidade;
	}
    }

    private class ServidorLeilao {
	private final String id;
	private final String type;
	private Double preco;
	private int quantidade;
	private String nextclient = "";
	private Map<String, Double> propostas = new HashMap<>();
	private final Lock lp = new ReentrantLock();
	private final Condition notBiggest = lp.newCondition();

	public ServidorLeilao(String id, String type, Double preco, int quantidade) {
            this.id = id;
            this.type = type;
            this.preco = preco;
            this.quantidade = quantidade;
        }

	public String getType() {
            return this.type;
	}

	public Double getPreco() {
            return this.preco;
	}

	public int getQuantidade() {
            return this.quantidade;
	}

	public void putProposta(String email, double valor) {
		this.propostas.put(email, valor);
		if (propostas.size() == 1) this.nextclient = email;
                calcMaiorLicitante();
	}

	public void removeProposta(String email) {
		this.propostas.remove(email);
	}

	/* Calcula o maior licitante entre as propostas e atualiza o preco para o seu valor */
	public void calcMaiorLicitante() {
		String next = "";
		Double res = 0.00;
		for (Map.Entry<String, Double> licitacao : this.propostas.entrySet()) {
                    if (licitacao.getValue() > res) {
			res = licitacao.getValue();
			next = licitacao.getKey();
                    }
		}
		this.nextclient = next;
	}
    }

    public Catalogo() {
	this.pedido = new HashMap<>();
	this.leilao = new HashMap<>();
	pedido.put("0.50", new Servidor("t1", "0.50", 0.50, 2));
	pedido.put("1.00", new Servidor("t2", "1.00", 1.00, 1));
	pedido.put("1.50", new Servidor("t3", "1.50", 1.50, 1));
	leilao.put("l0.50", new ServidorLeilao("t4", "l0.50", 0.00, 1));
	leilao.put("l1.00", new ServidorLeilao("t5", "l1.00", 0.00, 1));
	leilao.put("l1.50", new ServidorLeilao("t6", "l1.50", 0.00, 1));
    }

    public double getPrice(String id) {
	if (id.contains("l")) return this.leilao.get(id).getPreco();
            return this.pedido.get(id).getPreco();
    }

    public String reservaPedido(String type) {
	l.lock();
	try {
            /*while(ocupacaopedido == MAX) {
		pavaliable.await();
            }
            ocupacaopedido+=1;*/
            Servidor s = this.pedido.get(type);
            s.ls.lock();
            try {
		l.unlock();
                while (s.quantidade == 0) {
                    ServidorLeilao sl = this.leilao.get("l"+type);
                    if (sl.quantidade == 0)
                        s.notTaken.await();
                    else {
                        sl.quantidade -= 1;
                        sl.preco = s.getPreco();
                        return sl.getType();
                    }
                }
                s.quantidade -= 1;
                return s.getType();	
            }
            catch (NullPointerException e) {
                return "";
            }
            finally {
		s.ls.unlock();
            }
	}
        catch (InterruptedException e) {
            l.unlock();
            return "";
	}
    }

    public String reservaLeilao(String email, String type, Double valor) {
    	l.lock();
	try {
            ServidorLeilao sl = this.leilao.get(type);
            sl.lp.lock();
            try {
		l.unlock();
		sl.putProposta(email, valor);
		while (sl.quantidade == 0 || !email.equals(sl.nextclient)) {
                    sl.notBiggest.await();
		}
		sl.quantidade -= 1;
		sl.preco = valor;
		sl.removeProposta(email);
		return sl.getType();
            }
            finally {
                sl.lp.unlock();
            }
	}
        catch (InterruptedException e) {
            l.unlock();
            return "";
	}
    }

    public void libertaReservaLeilao(String id) {
	l.lock();
	try {
            ServidorLeilao s = this.leilao.get(id);
            s.lp.lock();
            try {
		l.unlock();
		s.quantidade += 1;
		s.calcMaiorLicitante();
		s.notBiggest.signalAll();
            }
            finally {
		s.lp.unlock();
            }
	}
        catch (Exception e) {
            l.unlock();
	}
    }

    public void libertaReservaPedido(String id) {
	l.lock();
	try {
            Servidor s = this.pedido.get(id);
            s.ls.lock();
            try {
		l.unlock();
		s.quantidade += 1;
		s.notTaken.signalAll();
            }
            finally {
		s.ls.unlock();
            }
	}
        catch (Exception e) {
            l.unlock();
	}
    }
}