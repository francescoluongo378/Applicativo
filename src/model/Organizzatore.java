package model;

public class Organizzatore extends Utente {
    public Organizzatore(Integer id, String nome, String email, String password) {
        super(id, nome, email, password, "organizzatore");
    }

}
