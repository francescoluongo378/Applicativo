package model;
public class Organizzatore extends Utente {
    private Hackathon hackathon;

    public Organizzatore(int id, String nome, String email) {
        super(id, nome, email, "organizzatore");
    }

    public Organizzatore(String nome, String email) {
        super(nome, email, "organizzatore");
    }

    public void setHackathon(Hackathon hackathon) {
        this.hackathon = hackathon;
    }

    public Hackathon getHackathon() {
        return hackathon;
    }
}

