//import static java.lang.Thread.sleep;
import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStreamReader;
import java.net.Socket;
//import java.net.ServerSocket;
import java.io.PrintWriter;
//import java.io.FileInputStream;
import java.net.SocketException;
//import java.util.Queue;


public class Reader implements Runnable{
    ClienteStub cs;
    Socket socket;
    BufferedReader in;
    PrintWriter out;

    public Reader(Socket socket, BufferedReader in, PrintWriter out, ClienteStub c){
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.cs = c;
    }

    //so executa n leitores de cada vez, em simultâneo.
    @Override
    public void run(){
        try{
            while(true) {
                String ans = in.readLine();
                String[] p = ans.split(" ");
                if (p[0].equals("all:")) {
                    System.out.println(ans);
                }
                else if (ans.equals("exit")) break;
                else {
                    cs.addMessage(ans);
                }
            }
            in.close();
            out.close();
        }
        catch(SocketException e){
        }

        catch(IOException s){
        }
    }
}