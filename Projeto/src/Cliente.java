//import Servidor.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author João Marques, Nuno Rei e Jaime Leite
 */

public class Cliente {
    //private final Map<String,Cliente> clientes;
    
    public static void main(String[] args) throws Exception {
        //if(true == ValidaCliente(args[0],args[1])) {

            ClienteStub c = new ClienteStub();

            /*
            System.out.println("Agora está conetado ao servidor principal");
            System.out.println("1 -> servers para reservar");
            System.out.println("2 -> servers para leiloar");
            System.out.println("3 -> logout");*/

            boolean f = true;

            while (f) {
                String s = System.console().readLine();
                String[] p = s.split(" ");
                switch(p[0]) {
                    case "regista":
                        int reg = c.registaCliente(p[1], p[2]);
                        if (reg == 0) System.out.println("Registado");
                        break;
                    case "autentica":
                        int aut = c.autenticaCliente(p[1], p[2]);
                        if (aut == 0) {
                            System.out.println("Autenticado com sucesso");
                            f = false;
                        }
                        break;
                    default:
                        System.out.println("Comando invalida.");
                }
            }
                //out.println(s);
                //String s1 = in.readLine();
                //if(s1 == null) break;
                //System.out.println(s1);

            System.out.println("Acabou");
        //} 
        //else {
            //System.out.println("Credenciais inválidas!");
        //}
    }

    /*public InterfaceC() {
        this.clientes = new HashMap<>();
    }
    
    public boolean ValidaCliente(String nome, String pass) {
        Cliente logged = clientes.get(nome);
        if(logged == null) return false;
        return logged.Verifica_Pass(pass);
    }*/
}
