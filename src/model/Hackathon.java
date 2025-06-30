package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Hackathon {
    private int id; // NECESSARIO PER DATABASE
    private String titolo;
    private String sede;
    private int maxPartecipanti;
    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;
    private LocalDateTime inizioIscrizioni;
    private LocalDateTime fineIscrizioni;
    private int maxTeam;
    private Classifica classifica;
    private List<Giudice> giudici;
    private List<Team> teams;

    // Costruttori
    public Hackathon(String titolo, String sede, int maxPartecipanti,
                     LocalDateTime dataInizio, LocalDateTime dataFine,
                     LocalDateTime inizioIscrizioni, LocalDateTime fineIscrizioni,
                     int maxTeam) {
        this.titolo = titolo;
        this.sede = sede;
        this.maxPartecipanti = maxPartecipanti;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.inizioIscrizioni = inizioIscrizioni;
        this.fineIscrizioni = fineIscrizioni;
        this.maxTeam = maxTeam;
        this.giudici = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.classifica = new Classifica();
    }

    public Hackathon() {
        this.giudici = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.classifica = new Classifica();
    }

    // Getter e Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public int getMaxPartecipanti() {
        return maxPartecipanti;
    }

    public void setMaxPartecipanti(int maxPartecipanti) {
        this.maxPartecipanti = maxPartecipanti;
    }

    public LocalDateTime getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDateTime dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDateTime getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDateTime dataFine) {
        this.dataFine = dataFine;
    }

    public LocalDateTime getInizioIscrizioni() {
        return inizioIscrizioni;
    }

    public void setInizioIscrizioni(LocalDateTime inizioIscrizioni) {
        this.inizioIscrizioni = inizioIscrizioni;
    }

    public LocalDateTime getFineIscrizioni() {
        return fineIscrizioni;
    }

    public void setFineIscrizioni(LocalDateTime fineIscrizioni) {
        this.fineIscrizioni = fineIscrizioni;
    }

    public int getMaxTeam() {
        return maxTeam;
    }

    public void setMaxTeam(int maxTeam) {
        this.maxTeam = maxTeam;
    }

    public Classifica getClassifica() {
        return classifica;
    }

    public List<Giudice> getGiudici() {
        return new ArrayList<>(giudici);
    }

    public List<Team> getTeams() {
        return new ArrayList<>(teams);
    }




    public void aggiungiTeam(Team team) {
        teams.add(team);
        classifica.aggiungiTeam(team);
    }

    public void aggiungiGiudice(Giudice giudice) {
        giudici.add(giudice);
    }

    public List<Partecipante> getPartecipanti() {
        return List.of();
    }
}
