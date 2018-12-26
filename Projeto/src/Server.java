package trabalhoSD;

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

public class Server{
    public static void main(String[] args) throws Exception{
    	Map<String, Socket> clientesConectados = new HashMap<>();
    	
    	ServerSocket ss = new ServerSocket(12345);
        ServidorStub st = new ServidorStub();
        while (true){
            Socket x;
            x = ss.accept();
            new Servidor(x,st,clientesConectados).start();
        }
    }
}