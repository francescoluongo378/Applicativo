package model;

public class Documento {
    private int id;
    private String titolo;
    private int idTeam;

    public Documento() {
    }

    public int getId() {
        return id;
    }

    public String getTitolo() {
        return titolo;
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

    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
    }
    
    @Override
    public String toString() {
        return titolo;
    }
}
