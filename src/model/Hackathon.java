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
    private final Classifica classifica;
    private final List<Giudice> giudici;
    private final List<Team> teams;
    private final List<Partecipante> partecipanti;

    // Costruttore
    public Hackathon() {
        this.giudici = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.partecipanti = new ArrayList<>();
        this.classifica = new Classifica();
        System.out.println("DEBUG: Costruttore default Hackathon, partecipanti inizializzato: " + true);
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

    public List<Partecipante> getPartecipanti() {
        System.out.println("DEBUG: getPartecipanti chiamato");

        if (partecipanti == null) {
            System.out.println("DEBUG: partecipanti è null, creando una nuova lista");
            return new ArrayList<>();
        }
        System.out.println("DEBUG: partecipanti non è null, tipo: " + partecipanti.getClass().getName());
        return partecipanti;
    }

}