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
    private long inicialTime = 0,finalTime = 0;

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
            String s;

            while ((s = in.readLine()) != null) {
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
                    case "pedir":
                        s = st.reservarPorPedido(p[1]);
                        inicialTime = System.currentTimeMillis();
                        break;
                    
                    case "libertar":
                        s = st.libertaReserva(p[1]);
                        
                        if(inicialTime == 0);
                        else finalTime = (System.currentTimeMillis()-inicialTime) / 1000;
                        st.setValorDivida(email,(finalTime * Double.parseDouble(s)));
                        
                        s = "Libertou o servidor";
                        
                        break;
                    
                    case "divida":
                        double divida = st.getValorDivida(email);
                        
                        s = "Tem uma divida de: " + divida + "euros";
                        
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
                if (p[0].equals("exit")) break;
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