import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author João Marques, Nuno Rei, Jaime Leite e Hugo Nogueira
 * @version 01-2019
 */
public class Servidor implements Runnable {
    private final Socket x;
    private final ServidorImpl st;
    private long inicialTime = 0,finalTime = 0;

    public Servidor(Socket x, ServidorImpl st){
        this.x = x;
        this.st = st;
    }

    public static void main(String[] args) throws Exception {
        Map<String, Socket> cc = new HashMap<>();
        ServerSocket ss = new ServerSocket(12345);
        ServidorImpl st = new ServidorImpl();
        while (true){
            Socket x;
            x = ss.accept();
            new Thread(new Servidor(x,st)).start();
        }
    }

    @Override
    public void run(){
        try{
            String email = null;
            PrintWriter out = new PrintWriter(x.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(x.getInputStream()));
            String s;
            while ((s = in.readLine()) != null){
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
                        if(st.temServidor(email) == 0) {
                            s = st.reservarPorPedido(email, p[1]);
                            inicialTime = System.currentTimeMillis();
                        }
                        else s = "Já tem um servidor";
                        break;

                    case "libertar":
                        s = st.libertaReserva(email, p[1]);
                        if(inicialTime == 0);
                        else if (!s.equals("")) {
                            finalTime = (System.currentTimeMillis() - inicialTime) / 1000;
                            st.setValorDivida(email, (finalTime * Double.parseDouble(s)));
                            s = "Libertou o servidor";
                        }
                        else s = "Identificador de Servidor invalido";
                        break;
                        
                    case "divida":
                        double divida = st.getValorDivida(email);
                        s = "Tem uma divida de: " + divida + "$";
                        break;
                        
                    case "leilao":
                        s = st.reservarLeilao(email, p[1], p[2]);
                        inicialTime = System.currentTimeMillis();
                        break;
                        
                    case "exit":
                        s = "exit";
                        double valor;
                        if((valor = st.temServidor(email)) != 0){
                            finalTime = (System.currentTimeMillis() - inicialTime) / 1000;
                            //preço a que o server foi reservado
                            st.setValorDivida(email, (finalTime * valor));
                        }
                        break;
                }
                out.println(s);
                out.flush();
                if (p[0].equals("exit")) break;
            }
            
            //se fizer Control^C e tiver um servidor em sua posse
            double valor = 0.0;
            if((valor = st.temServidor(email)) != 0){
                finalTime = (System.currentTimeMillis() - inicialTime) / 1000;
                //preço a que o server foi reservado
                st.setValorDivida(email, (finalTime * valor));
            }

            st.logOut(email);
            out.close();
            in.close();
        }
        catch (IOException  e){
            e.printStackTrace();
        }
    }
}