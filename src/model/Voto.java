package model;
// Voto.java
public class Voto {
    private int id; // opzionale
    private int idTeam;
    private int idGiudice;
    private int punteggio;

    public Voto() {} // costruttore vuoto

    public Voto(int idTeam, int idGiudice, int punteggio) {
        this.idTeam = idTeam;
        this.idGiudice = idGiudice;
        this.punteggio = punteggio;
    }

    // getter e setter
    public int getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
    }

    public int getIdGiudice() {
        return idGiudice;
    }

    public void setIdGiudice(int idGiudice) {
        this.idGiudice = idGiudice;
    }

    public int getPunteggio() {
        return punteggio;
    }

    public void setPunteggio(int punteggio) {
        this.punteggio = punteggio;
    }
}
