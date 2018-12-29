import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author João Marques, Nuno Rei, Jaime Leite e Hugo Nogueira
 */
public class ClienteStub implements interfaceGlobal{
    private final Socket x;
    private final PrintWriter out;
    private final BufferedReader in;
    private Queue<String> received;

    /*
    private String email;
    private String password;
    private float value_to_pay;*/

    public ClienteStub() throws Exception{
        x = new Socket("localhost",12345);
        out = new PrintWriter(x.getOutputStream());
        in = new BufferedReader(new InputStreamReader(x.getInputStream()));
        received = new LinkedList<>();
        Thread leitorCliente = new Thread(new Reader(x,in,out,received));
        leitorCliente.start();
    }

    //Criação de um Cliente
    @Override
    public int registaCliente(String email, String pass){
        String pedido = "regista ";
        pedido+=email;
        pedido+= " ";
        pedido+=pass;

        out.println(pedido);
        out.flush();

        while (received.isEmpty());
        String resposta = received.remove();
        /*try {
            resposta = in.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/
        return Integer.parseInt(resposta);
    }

    @Override
    public int autenticaCliente(String email, String pass){
        String pedido = "autentica ";
        pedido+=email;
        pedido+= " ";
        pedido+=pass;

        out.println(pedido);
        out.flush();

        while (received.isEmpty());
        String resposta = received.remove();

        /*try {
            resposta = in.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/
        return Integer.parseInt(resposta);
    }

    @Override
     public String reservarPorPedido(String type){
        String pedido = "pedir ";
        pedido+=type;

        out.println(pedido);
        out.flush();

         while (received.isEmpty());
         String resposta = received.remove();

         /*try {
            resposta = in.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/
        return resposta;
    }

    @Override
    public String libertaReserva(String id){
        String pedido = "libertar ";
        pedido+=id;

        out.println(pedido);
        out.flush();

        while (received.isEmpty());
        String resposta = received.remove();

         /*try {
            resposta = in.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/
        return resposta;
    }

    public int reservarPorLeilao(String email, double quantia, String type){
        String pedido = "servidor_Leilao ";
        pedido+=email;
        pedido+= " ";
        pedido+=quantia;
        pedido+= " ";
        pedido+=type;

        out.println(pedido);
        out.flush();

        while (received.isEmpty());
        String resposta = received.remove();

        /*try {
            resposta = in.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/
        return Integer.parseInt(resposta);
    }

    public int retiraServidorExit(String email){
        String pedido = "exit ";
        pedido+=email;

        out.println(pedido);
        out.flush();

        while (received.isEmpty());
        String resposta = received.remove();
        /*try {
            resposta = in.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/
        return Integer.parseInt(resposta);
    }

    public int retiraServidorNull(){
        String pedido = null;

        out.println(pedido);
        out.flush();

        while (received.isEmpty());
        String resposta = received.remove();

        /*try {
            resposta = in.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/
        return Integer.parseInt(resposta);
    }

    //@Override
    public String leilao () {
        String pedido = "leilao ";

        out.println(pedido);
        out.flush();

        while (received.isEmpty());
        String resposta = received.remove();
        /*try {
            resposta = in.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/
        return resposta;
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
}

