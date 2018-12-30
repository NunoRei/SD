/**
 * @author João Marques, Nuno Rei, Jaime Leite e Hugo Nogueira
 */

public interface interfaceGlobal {
    int registaCliente(String email, String pass);
    int autenticaCliente(String email, String pass);
    String reservarPorPedido(String email, String type);
    String libertaReserva(String email, String id);
}
