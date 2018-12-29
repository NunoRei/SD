import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.HashMap;

public class Servidor implements Runnable {

    private final Socket x;
    private ServidorStub st;

    public Servidor(Socket x, ServidorStub st){
        this.x = x;
        this.st = st;
    }

    public static void main(String[] args) throws Exception {
        Map<String, Socket> cc = new HashMap<>();
        ServerSocket ss = new ServerSocket(12345);
        ServidorStub st = new ServidorStub();
        while (true){
            Socket x;
            x = ss.accept();
            new Thread(new Servidor(x,st)).start();
        }
    }

    public void run(){
        try {
            String email = null;
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
                        if (resautentica == 0) {
                            email = p[1];
                            st.clientesativos.put(p[1],x);
                        }
                        s = Integer.toString(resautentica);
                        break;
                    case "leilao": // So um teste de fazer broadcast de mensagens NAO E O FUNCIONAMENTO DO LEILAO
                        for (Socket sk : st.clientesativos.values()) {
                            if (sk != x) {
                                PrintWriter skout = new PrintWriter(sk.getOutputStream());
                                skout.println("all: Leilao iniciado");
                                skout.flush();
                            }
                        }
                        s = "licitacao feita";
                        break;
                    case "exit":
                        s = "exit";
                        break;
                    default:

                }
                out.println(s);
                out.flush();
                if (s.equals("exit")) break;
            }
            st.clientesativos.remove(email);
            out.close();
            in.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}


