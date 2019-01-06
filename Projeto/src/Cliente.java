/**
 *
 * @author João Marques, Nuno Rei, Jaime Leite e Hugo Nogueira
 * @version 01-2019
 */
public class Cliente {
    
    public static void main(String[] args) throws Exception {
        ClienteStub c = new ClienteStub();
        String email = null;
        int exit = 0;
        while (true) {
            String s = System.console().readLine();
            String[] p = s.split(" ");
            if(email == null){
                switch (p[0]) {
                    case "regista":
                        try {
                            int reg = c.registaCliente(p[1], p[2]);
                            if (reg == 0)
                                System.out.println("Registado.");
                            if (reg == 1)
                                System.out.println("Cliente já registado.");
                        }
                        catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("Coloque 'regista <email> <pass>.");
                        }
                        break;
                        
                    case "autentica":
                        try {
                            int aut = c.autenticaCliente(p[1], p[2]);
                            if (aut == 0) {
                                email = p[1];
                                System.out.println("Autenticado com sucesso.");
                            }
                            if (aut == 1) System.out.println("Cliente nao registado.");
                            if (aut == 2) System.out.println("Password invalida.");
                            if (aut == 3) System.out.println("Cliente ja autenticado.");
                        }
                        catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("Coloque 'autentica <email> <pass>'.");
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
                        try {
                            s = c.reservarPorPedido(email,p[1]);
                            System.out.println(s);
                        }
                        catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("Coloque 'pedir <type_server>'.");
                        }
                        break;
                        
                    case "leilao":
                        try {
                            String resulleilao = c.reservarLeilao(email,p[1],p[2]);
                            System.out.println(resulleilao);
                        }
                        catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("Coloque 'leilao <type_server> <value>'.");
                        }
                        break;
                        
                    case "libertar":
                        try {
                            s = c.libertaReserva(email, p[1]);
                            System.out.println(s);
                        }
                        catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("Coloque 'libertar <id_server>'.");
                        }
                        break;
                        
                    case "divida":
                        s = c.obterDivida();
                        System.out.println(s+".");
                        break;
                        
                    case "exit":
                        exit = 1;
                        break;
                        
                    default:
                        System.out.println("Comando invalido.");
                        break;
                }
            }
            if (exit == 1) {
                c.exit();
                break;
            }
        }
        System.out.println("Exiting...");
    }
}