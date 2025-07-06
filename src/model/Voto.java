package model;

public class Voto {
    private int id;         // opzionale, id nel database
    private final int idTeam;
    private final int idGiudice;
    private final int punteggio;

    // costruttore senza id (nuovo voto)
    public Voto(int idTeam, int idGiudice, int punteggio) {
        this.idTeam = idTeam;
        this.idGiudice = idGiudice;
        this.punteggio = punteggio;
    }

    // getter e setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTeam() {
        return idTeam;
    }

    public int getIdGiudice() {
        return idGiudice;
    }

    public int getPunteggio() {
        return punteggio;
    }

    @Override
    public String toString() {
        return "Voto{" +
                "id=" + id +
                ", idTeam=" + idTeam +
                ", idGiudice=" + idGiudice +
                ", punteggio=" + punteggio +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Voto voto)) return false;
        return idTeam == voto.idTeam &&
                idGiudice == voto.idGiudice &&
                punteggio == voto.punteggio;
    }

    @Override
    public int hashCode() {
        int result = idTeam;
        result = 31 * result + idGiudice;
        result = 31 * result + punteggio;
        return result;
    }
}
