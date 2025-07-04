package model;

public class Partecipante extends Utente {
    private int teamId;

    public Partecipante(String nome, String email, String password) {
        super(nome, email, password, "partecipante");
    }

    public Partecipante(Integer id, String nome, String email, String password) {
        super(id, nome, email, password, "partecipante");
    }

    public Partecipante(int id, String nome, String email) {
        super(id, nome, email);
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}

