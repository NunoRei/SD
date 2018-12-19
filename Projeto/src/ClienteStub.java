import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author João Marques, Nuno Rei e Jaime Leite
 */
public class ClienteStub implements interfaceGlobal {

    private final Socket x;
    private final PrintWriter out;
    private final BufferedReader in;
    /*
    private String email;
    private String password;
    private float value_to_pay;*/

    public ClienteStub () throws Exception {
        x = new Socket("localhost",12345);
        out = new PrintWriter(x.getOutputStream());
        in = new BufferedReader(new InputStreamReader(x.getInputStream()));
    }

    //Criação de um Cliente
    @Override
    public int registaCliente (String email, String pass) {
        String pedido = "regista ";
        pedido+=email;
        pedido+= " ";
        pedido+=pass;

        out.println(pedido);
        out.flush();

        String resposta = null;
        try {
            resposta = in.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(resposta);
    }

    @Override
    public int autenticaCliente (String email, String pass) {
        String pedido = "autentica ";
        pedido+=email;
        pedido+= " ";
        pedido+=pass;

        out.println(pedido);
        out.flush();

        String resposta = null;
        try {
            resposta = in.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(resposta);
    }
    //Verifica palavra passe
    public boolean Verifica_Pass(String pass) {
        return true;
        /*return this.password.equals(pass);*/
    }
}