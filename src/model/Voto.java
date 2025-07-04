package model;

public class Voto {
    private int id;         // opzionale, id nel database
    private int idTeam;
    private int idGiudice;
    private int punteggio;

    public Voto() {
        // costruttore vuoto
    }

    // costruttore senza id (nuovo voto)
    public Voto(int idTeam, int idGiudice, int punteggio) {
        this.idTeam = idTeam;
        this.idGiudice = idGiudice;
        this.punteggio = punteggio;
    }

    // costruttore completo con id
    public Voto(int id, int idTeam, int idGiudice, int punteggio) {
        this.id = id;
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
        if (!(o instanceof Voto)) return false;
        Voto voto = (Voto) o;
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
