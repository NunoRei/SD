import java.io.BufferedReader;
//import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author João Marques, Nuno Rei, Jaime Leite e Hugo Nogueira
 */
public class ClienteStub implements interfaceGlobal{
    private final Socket x;
    private final PrintWriter out;
    private final BufferedReader in;
    private int qsize = 0;
    private final Queue<String> received;
    private final Lock l = new ReentrantLock();
    private final Condition notEmpty = l.newCondition();

    /*
    private String email;
    private String password;
    private float value_to_pay;*/

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
        //while (received.isEmpty());
        String resposta;
        int res = 0;
        try {
            resposta = takeMessage();
            res = Integer.parseInt(resposta);
        }
        catch (InterruptedException | NumberFormatException e) {}
        /*try {
            resposta = in.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/
        
        return res;
    }

    @Override
    public int autenticaCliente(String email, String pass){
        String pedido = "autentica "+email+" "+pass;
        out.println(pedido);
        out.flush();
        //while (received.isEmpty());
        String resposta;
        int res = 0;
        try {
            resposta = takeMessage();
            res = Integer.parseInt(resposta);
        }
        catch (InterruptedException e) {}
        /*try {
            resposta = in.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/
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
         /*try {
            resposta = in.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/
        return resposta;
    }

    @Override
    public String libertaReserva(String email, String id){
        String pedido = "libertar "+id;
        out.println(pedido);
        out.flush();
        //while (received.isEmpty());
        String resposta = null;
        try {
            resposta = takeMessage();
        }
        catch (InterruptedException e) {}
        /*try {
            resposta = in.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/
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
        /*try {
            resposta = in.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/
        return resposta;
    }

    public String reservarLeilao(String email, String type, String valor){
        String pedido = "leilao";
        pedido+= " ";
        pedido+=type;
        pedido+= " ";
        pedido+=valor;

        out.println(pedido);
        out.flush();

        String resposta = null;
        try {
            resposta = takeMessage();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        /*try {
            resposta = in.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/
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
        /*try {
            resposta = in.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/
        return res;
    }

    public void exit () {
        String pedido = "exit";
        out.println(pedido);
        out.flush();
    }
    
    //Verifica palavra passe
    public boolean Verifica_Pass(String pass){
        return true;
        /*return this.password.equals(pass);*/
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