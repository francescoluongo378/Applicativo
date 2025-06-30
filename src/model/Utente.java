package model;

public class Utente {
    private Integer id;
    private String nome;
    private String email;
    private String password;
    private String ruolo;

    // 4-arg per registrazione
    public Utente(String nome, String email, String password, String ruolo) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
    }

    // 5-arg per lettura dal DB
    public Utente(Integer id, String nome, String email, String password, String ruolo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
    }

    public Utente(int id, String nome, String email, String organizzatore) {
    }

    public Utente(String nome, String email, String organizzatore) {
    }

    public Utente(int id, String nome, String email) {
    }

    public Integer getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getRuolo() {
        return ruolo;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public void setPassword(String nuovaPassword) {
    }
}

