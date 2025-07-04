package model;

public class Giudice extends Utente {

    public Giudice(Integer id, String nome, String email, String password) {
        super(id, nome, email, password, "giudice");
    }

    public Giudice(int id, String nome, String email) {
        super(id, nome, email);
    }
}
