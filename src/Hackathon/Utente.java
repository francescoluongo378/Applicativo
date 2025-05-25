package Hackathon;

public class Utente {
    protected String id;
    protected String nome;
    protected String email;

    public Utente(String id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }
    public void registrati() {
        System.out.println(nome + " si è registrato.");
    }



}
