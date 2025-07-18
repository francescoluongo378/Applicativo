package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Hackathon {
    /** Identificativo univoco dell'Hackathon, necessario per il database */
    private int id;

    /** Titolo dell'evento */
    private String titolo;

    /** Luogo dove si svolge l'evento */
    private String sede;

    /** Numero massimo di partecipanti ammessi */
    private int maxPartecipanti;

    /** Data e ora di inizio dell'Hackathon */
    private LocalDateTime dataInizio;

    /** Data e ora di fine dell'Hackathon */
    private LocalDateTime dataFine;

    /** Data e ora di apertura delle iscrizioni */
    private LocalDateTime inizioIscrizioni;

    /** Data e ora di chiusura delle iscrizioni */
    private LocalDateTime fineIscrizioni;

    /** Numero massimo di team ammessi */
    private int maxTeam;

    /** Classifica dei team partecipanti */
    private final Classifica classifica;

    /** Lista dei giudici assegnati all'Hackathon */
    private final List<Giudice> giudici;

    /** Lista dei team partecipanti */
    private final List<Team> teams;

    /** Lista dei partecipanti iscritti */
    private final List<Partecipante> partecipanti;

    /**
     * Costruttore di default.
     * <p>
     * Inizializza le liste di giudici, team e partecipanti come ArrayList vuoti.
     * Crea anche una nuova istanza di Classifica.
     * </p>
     */
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

    /**
     * Restituisce una copia della lista dei giudici assegnati all'Hackathon.
     * <p>
     * Viene restituita una copia per evitare modifiche non controllate alla lista originale.
     * </p>
     *
     * @return Copia della lista dei giudici
     */
    public List<Giudice> getGiudici() {
        return new ArrayList<>(giudici);
    }

    /**
     * Restituisce una copia della lista dei team partecipanti all'Hackathon.
     * <p>
     * Viene restituita una copia per evitare modifiche non controllate alla lista originale.
     * </p>
     *
     * @return Copia della lista dei team
     */
    public List<Team> getTeams() {
        return new ArrayList<>(teams);
    }

    /**
     * Aggiunge un team alla lista dei team partecipanti e alla classifica.
     * <p>
     * Questo metodo aggiorna sia la lista interna dei team sia la classifica
     * associata all'Hackathon.
     * </p>
     *
     * @param team Team da aggiungere
     */
    public void aggiungiTeam(Team team) {
        teams.add(team);
        classifica.aggiungiTeam(team);
    }

    /**
     * Restituisce la lista dei partecipanti iscritti all'Hackathon.
     * <p>
     * A differenza dei metodi getGiudici() e getTeams(), questo metodo
     * restituisce la lista originale e non una copia. Questo è un design
     * intenzionale per permettere modifiche dirette alla lista.
     * </p>
     *
     * @return Lista dei partecipanti
     */
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