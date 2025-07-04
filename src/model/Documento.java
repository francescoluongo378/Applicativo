package model;

public class Documento {
    private int id;
    private String titolo;
    private int idPartecipante;
    private int idTeam;

    public Documento() {
    }

    public Documento(int id, String titolo, int idTeam) {
        this.id = id;
        this.titolo = titolo;
        this.idTeam = idTeam;
    }

    public int getId() {
        return id;
    }

    public String getTitolo() {
        return titolo;
    }

    public int getIdPartecipante() {
        return idPartecipante;
    }

    public int getIdTeam() {
        return idTeam;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setIdPartecipante(int idPartecipante) {
        this.idPartecipante = idPartecipante;
    }

    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
    }
    
    @Override
    public String toString() {
        return titolo;
    }
}
