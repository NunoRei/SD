import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author João Marques, Nuno Rei, Jaime Leite e Hugo Nogueira
 */

public class Cliente {

    public static void main(String[] args) throws Exception {

        ClienteStub c = new ClienteStub();

        int autenticado = 0;
        int exit = 0;
        long inicialTime = 0, finalTime = 0;

        while (true) {
            String s = System.console().readLine();
            String[] p = s.split(" ");
            if (autenticado == 0) {
                switch (p[0]) {
                    case "regista":
                        int reg = c.registaCliente(p[1], p[2]);
                        if (reg == 0) System.out.println("Registado");
                        break;
                    case "autentica":
                        int aut = c.autenticaCliente(p[1], p[2]);
                        if (aut == 0) {
                            autenticado = 1;
                            System.out.println("Autenticado com sucesso");
                        }
                        break;
                    case "exit":
                        exit = 1;
                        break;
                    default:
                        System.out.println("Comando invalido.");
                        break;
                }
            }
            else {
                switch (p[0]) {
                    case "pedir":
                        s = c.reservarPorPedido(p[1]);
                        System.out.println(s);
                        /*int reg = c.registaCliente(p[1], p[2]);
                        if (reg == 0) System.out.println("Registado");*/
                        /*System.out.println("Registado");
                        inicialTime = System.currentTimeMillis();*/
                        break;
                    case "leilao":
                        /*int aut = c.autenticaCliente(p[1], p[2]);
                        if (aut == 0) {
                            System.out.println("Autenticado com sucesso");
                        }*/
                        String resulleilao = c.leilao();
                        System.out.println(resulleilao);
                        break;
                    case "libertar":
                        s = c.libertaReserva(p[1]);
                        System.out.println(s);
                        /*int a = c.autenticaCliente(p[1], p[2]);
                        if (a == 0) {
                            System.out.println("Autenticado com sucesso");
                        }*/
                        /*if (inicialTime == 0);
                        else finalTime = System.currentTimeMillis()-inicialTime;
                        System.out.println("Está com uma divida de " + finalTime);*/
                        break;
                    case "divida":
                        System.out.println("Registado");
                        break;
                    case "exit":
                        exit = 1;
                        // remover dos clientes conectados
                        break;
                    default:
                        System.out.println("Comando invalido.");
                        break;
                }
            }
            if (exit == 1) break;
        }
        c.exit();
        System.out.println("Exiting...");
    }
}



