package model;

public class Giudice extends Utente {
    // nuovo (registrazione)
    public Giudice(String nome, String email, String password) {
        super(nome, email, password, "giudice");
    }

    // da DB (con id)
    public Giudice(Integer id, String nome, String email, String password) {
        super(id, nome, email, password, "giudice");
    }

    public Giudice(int id, String nome, String email) {
        super(id,nome,email);
    }
}
