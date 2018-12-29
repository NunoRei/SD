import static java.lang.Thread.sleep;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.net.SocketException;
import java.util.Queue;


public class Reader implements Runnable{
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    Queue<String> received;

    public Reader(Socket socket, BufferedReader in, PrintWriter out, Queue<String> q){
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.received = q;
    }

    //so executa n leitores de cada vez, em simultâneo.
    public void run(){
        String ans;
        try{
            while((ans = this.in.readLine()) != null) {
                String[] p = ans.split(" ");
                if (p[0].equals("all:")) {
                    System.out.println(ans);
                }
                else if (ans.equals("exit")) break;
                else {
                    received.add(ans);
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

