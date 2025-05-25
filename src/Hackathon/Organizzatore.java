package Hackathon;

public class Organizzatore extends Utente {
    private Hackathon hackathon;


    public Organizzatore(String id, String nome, String email) {
        super(id, nome, email);


        } public void setHackathon(Hackathon hackathon) {
        this.hackathon = hackathon;
    }
    }

