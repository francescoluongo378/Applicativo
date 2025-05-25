package Hackathon;

public class Partecipante extends Utente {
    private Team team;

    public Partecipante(String id, String nome, String email) {
        super(id,nome, email);
    }

    public void partecipaAlTeam(Team team) {
        if (team.aggiungiMembro(this)) {
            this.team = team;
        }
    }

    public Team getTeam() {
        return team;
    }

    }
