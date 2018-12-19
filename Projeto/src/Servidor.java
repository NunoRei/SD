import java.util.HashMap;
import java.util.Map;
//import Cliente.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author João Marques, Nuno Rei e Jaime Leite
 */

public class Servidor implements Runnable {

    //private Servidor[] servidores = new Servidor[30]; //ports [1200;1229]
    private int[] ocupados = new int[30]; //0-> livre, 1-> ocupado, 2-> leilão
    private final Socket x;
    private ServidorStub st;

    public Servidor(Socket x, ServidorStub st) {
        this.x = x;
        this.st = st;
    }

    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(12345);
        ServidorStub st = new ServidorStub();
        while (true) {
            Socket x = ss.accept();
            new Thread(new Servidor(x,st)).start();
        }
    }

    public void run() {

        try {
            PrintWriter out = new PrintWriter(x.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(x.getInputStream()));
            while (true) {
                String s = in.readLine();
                String[] p = s.split(" ");
                switch(p[0]) {
                    case "regista":
                        int resregista = st.registaCliente(p[1], p[2]);
                        s = Integer.toString(resregista);
                        break;
                    case "autentica":
                        int resautentica = st.autenticaCliente(p[1], p[2]);
                        s = Integer.toString(resautentica);
                        break;
                    default:
                        System.out.println("Comando invalida.");
                }
                //if (s.equals("3")) break;
                out.println(s);
                out.flush();
            }
            //out.close();
            //x.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
