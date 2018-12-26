import static java.lang.Thread.sleep;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.net.SocketException;


public class Reader implements Runnable{
    Socket socket;
    BufferedReader in;
    PrintWriter out;

    public Reader(Socket socket, BufferedReader in, PrintWriter out){
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    //so executa n leitores de cada vez, em simultâneo.
    public void run(){
        String ans;

    	try{
            while((ans = this.in.readLine()) != null){
                System.out.println(ans);
            }
        }
        catch(SocketException e){
        }

        catch(IOException s){ 
        }
    }
}

