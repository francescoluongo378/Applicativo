package Hackathon;

public class Voto {
    private Giudice giudice;
    private Team team;
    private int punteggio;

    public Voto(Giudice giudice, Team team, int punteggio) {
        this.giudice = giudice;
        this.team = team;
        this.punteggio = punteggio;

    }

    public int getPunteggio() {
        return punteggio;
    }
}
