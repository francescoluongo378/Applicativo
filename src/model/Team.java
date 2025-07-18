package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un team partecipante a un Hackathon.
 * <p>
 * Un team è composto da un massimo di 3 partecipanti che lavorano insieme
 * a un progetto. Il limite di 3 partecipanti è implementato tramite controlli
 * nel Controller e nella GUI, non nella classe Team stessa.
 * </p>
 *
 */
public class Team {
    /** Identificativo univoco del team */
    private int id;

    /** Nome del team */
    private String nome;

    /** Descrizione del progetto o del team */
    private String descrizione;

    /** Percentuale di progresso del team */
    private int progresso;

    /** Riferimento all'Hackathon a cui il team partecipa */
    private Hackathon hackathon;

    /** Lista dei voti ricevuti dai giudici */
    private List<Voto> voti;

    /**
     * Costruttore di default.
     * Inizializza una lista vuota di voti e progresso a 0.
     */
    public Team() {
        voti = new ArrayList<>();
        progresso = 0;
    }

    /**
     * Costruttore con parametri.
     *
     * @param id Identificativo del team
     * @param nome Nome del team
     * @param hackathon Hackathon a cui il team partecipa
     */
    public Team(int id, String nome, Hackathon hackathon) {
        this.id = id;
        this.nome = nome;
        this.hackathon = hackathon;
        this.voti = new ArrayList<>();
        this.progresso = 0;
    }

    /**
     * Costruttore con parametri completi.
     *
     * @param id Identificativo del team
     * @param nome Nome del team
     * @param descrizione Descrizione del team
     * @param progresso Percentuale di progresso del team
     * @param hackathon Hackathon a cui il team partecipa
     */
    public Team(int id, String nome, String descrizione, int progresso, Hackathon hackathon) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.progresso = progresso;
        this.hackathon = hackathon;
        this.voti = new ArrayList<>();
    }

    // Getter e setter

    /**
     * Restituisce l'ID del team.
     *
     * @return ID del team
     */
    public int getId() { return id; }

    /**
     * Imposta l'ID del team.
     *
     * @param id Nuovo ID del team
     */
    public void setId(int id) { this.id = id; }

    /**
     * Restituisce il nome del team.
     *
     * @return Nome del team
     */
    public String getNome() { return nome; }

    /**
     * Imposta il nome del team.
     *
     * @param nome Nuovo nome del team
     */
    public void setNome(String nome) { this.nome = nome; }

    /**
     * Restituisce la descrizione del team o del progetto.
     *
     * @return Descrizione del team
     */
    public String getDescrizione() { return descrizione; }

    /**
     * Imposta la descrizione del team o del progetto.
     *
     * @param descrizione Nuova descrizione del team
     */
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    /**
     * Restituisce la percentuale di progresso del team.
     *
     * @return Percentuale di progresso del team
     */
    public int getProgresso() { return progresso; }

    /**
     * Imposta la percentuale di progresso del team.
     *
     * @param progresso Nuova percentuale di progresso del team
     */
    public void setProgresso(int progresso) { this.progresso = progresso; }

    public Hackathon getHackathon() { return hackathon; }

    /**
     * Imposta l'Hackathon a cui il team partecipa.
     *
     * @param hackathon Nuovo oggetto Hackathon
     */
    public void setHackathon(Hackathon hackathon) { this.hackathon = hackathon; }

    /**
     * Restituisce la lista dei voti ricevuti dai giudici.
     *
     * @return Lista di oggetti Voto
     */
    public List<Voto> getVoti() { return voti; }

    /**
     * Imposta la lista dei voti ricevuti dai giudici.
     *
     * @param voti Nuova lista di oggetti Voto
     */
    public void setVoti(List<Voto> voti) { this.voti = voti; }

    /**
     * Calcola la media dei punteggi ricevuti dal team.
     *
     * @return Media dei punteggi, 0.0 se non ci sono voti
     */
    public double getPunteggioMedio() {
        if (voti.isEmpty()) {
            return 0.0;
        }

        int somma = 0;
        for (Voto v : voti) {
            somma += v.getPunteggio();
        }
        return (double) somma / voti.size();
    }
}