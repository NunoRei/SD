package Cliente;
/**
 *
 * @author João Marques, Nuno Rei e Jaime Leite
 */
public class Cliente {
    private String user_name;
    private String password;
    private float value_to_pay;
    
    //Criação de um Cliente
    public Cliente (String nome, String pass) {
        this.user_name = nome;
        this.password = pass;
        this.value_to_pay = 0;
    }
    
    //Verifica palavra passe
    public boolean Verifica_Pass(String pass) {
        return this.password.equals(pass);
    }
    
    //Retornar valor a dever
    public float getValue_to_Price() {
        return this.value_to_pay;
    }
}
