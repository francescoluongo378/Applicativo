package Hackathon;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Hackathon {
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
    private List<Team> Teams;

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
        this.Teams = new ArrayList<>();
        this.classifica = new Classifica();
    }



    public String getTitolo() { return titolo; }
    public String getSede() { return sede; }
    public int getMaxPartecipanti() { return maxPartecipanti; }
    public LocalDateTime getDataInizio() { return dataInizio; }
    public LocalDateTime getDataFine() { return dataFine; }
    public LocalDateTime getInizioIscrizioni() { return inizioIscrizioni; }
    public LocalDateTime getFineIscrizioni() { return fineIscrizioni; }
    public int getMaxTeam() { return maxTeam; }
    public List<Giudice> getGiudici() { return new ArrayList<>(giudici); }
    public List<Team> getTeams() { return new ArrayList<>(Teams); }
    public Classifica getClassifica() { return classifica; }
}



