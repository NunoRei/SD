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
public class InterfaceS {
    //private Servidor[] servidores = new Servidor[30]; //ports [1200;1229]
    private int[] ocupados = new int[30]; //0-> livre, 1-> ocupado, 2-> leilão
    private static ServerSocket SS;
    
    public static void main(String[] args) throws IOException {
        SS = new ServerSocket(1234);
        Socket x;
        while (true) {
            x = SS.accept();
            PrintWriter out = new PrintWriter(x.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(x.getInputStream()));
            while (true) {
                String s = in.readLine();
                if(s.equals("3")) break;
                out.println(s);
                out.flush();
            }
            out.close();
            x.close();
            //removeCliente();
            break;
        }
    }
}
