/**
 *
 * @author João Marques, Nuno Rei, Jaime Leite e Hugo Nogueira
 * @version 01-2019
 */
public interface interfaceGlobal {
    int registaCliente(String email, String pass);
    int autenticaCliente(String email, String pass);
    String reservarPorPedido(String email, String type);
    String libertaReserva(String email, String id);
    String reservarLeilao(String email, String type, String valor);
}
