import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author João Marques, Nuno Rei, Jaime Leite e Hugo Nogueira
 * @version 01-2019
 */
public class ClienteStub implements interfaceGlobal{
    private final Socket x;
    private final PrintWriter out;
    private final BufferedReader in;
    private int qsize = 0;
    private final Queue<String> received;
    private final Lock l = new ReentrantLock();
    private final Condition notEmpty = l.newCondition();

    public ClienteStub() throws Exception{
        x = new Socket("localhost",12345);
        out = new PrintWriter(x.getOutputStream());
        in = new BufferedReader(new InputStreamReader(x.getInputStream()));
        received = new LinkedList<>();
        Thread leitorCliente = new Thread(new Reader(x,in,out,this));
        leitorCliente.start();
    }

    //Criação de um Cliente
    @Override
    public int registaCliente(String email, String pass){
        String pedido = "regista "+email+" "+pass;
        out.println(pedido);
        out.flush();
        String resposta;
        int res = 0;
        try {
            resposta = takeMessage();
            res = Integer.parseInt(resposta);
        }
        catch (InterruptedException | NumberFormatException e) {}
        return res;
    }

    @Override
    public int autenticaCliente(String email, String pass){
        String pedido = "autentica "+email+" "+pass;
        out.println(pedido);
        out.flush();
        String resposta;
        int res = 0;
        try {
            resposta = takeMessage();
            res = Integer.parseInt(resposta);
        }
        catch (InterruptedException e) {}
        return res;
    }

    @Override
     public String reservarPorPedido(String email, String type){
        String pedido = "pedir "+type;
        out.println(pedido);
        out.flush();
        String resposta = null;
        try {
            resposta = takeMessage();
        }
        catch (InterruptedException e) {}
        return resposta;
    }

    @Override
    public String libertaReserva(String email, String id){
        String pedido = "libertar "+id;
        out.println(pedido);
        out.flush();
        String resposta = null;
        try {
            resposta = takeMessage();
        }
        catch (InterruptedException e) {}
        return resposta;
    }
    
    public String obterDivida(){
        String pedido = "divida ";
        out.println(pedido);
        out.flush();
        String resposta = null;
        try {
            resposta = takeMessage();
        }
        catch (InterruptedException e) {}
        return resposta;
    }

    @Override
    public String reservarLeilao(String email, String type, String valor){
        String pedido = "leilao "+type+" "+valor;
        out.println(pedido);
        out.flush();
        String resposta = null;
        try {
            resposta = takeMessage();
        }
        catch (InterruptedException e) {}
        return resposta;
    }

    @SuppressWarnings("empty-statement")
    public int retiraServidorExit(String email){
        String pedido = "exit "+email;
        out.println(pedido);
        out.flush();
        while (received.isEmpty());
        String resposta = received.remove();
        int res = 0;
        try {
            res = Integer.parseInt(resposta);
        }
        catch (NumberFormatException e) {}
        return res;
    }

    @SuppressWarnings("empty-statement")
    public int retiraServidorNull(){
        String pedido = null;
        out.println(pedido);
        out.flush();
        while (received.isEmpty());
        String resposta = received.remove();
        int res = 0;
        try {
            res = Integer.parseInt(resposta);
        }
        catch (NumberFormatException e) {}
        return res;
    }

    public void exit () {
        String pedido = "exit";
        out.println(pedido);
        out.flush();
    }

    public void addMessage(String s) {
        l.lock();
        try {
            received.add(s);
            qsize += 1;
            notEmpty.signalAll();
        }
        finally {
            l.unlock();
        }
    }

    public String takeMessage() throws InterruptedException {
        l.lock();
        try {
            while (qsize == 0) {
                notEmpty.await();
            }
            String res;
            res = received.remove();
            qsize -= 1;
            return res;
        }
        finally {
            l.unlock();
        }
    }
}