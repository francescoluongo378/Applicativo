package model;

/**
 * Rappresenta la valutazione assegnata da un giudice a un team.
 * <p>
 * Un voto è caratterizzato da:
 * <ul>
 *   <li>ID del team valutato</li>
 *   <li>ID del giudice che ha assegnato il voto</li>
 *   <li>Punteggio assegnato (tipicamente da 1 a 10)</li>
 * </ul>
 * </p>
 * <p>
 * Gli attributi idTeam, idGiudice e punteggio sono dichiarati come {@code final}
 * per garantire l'immutabilità del voto una volta creato. Questo è importante
 * per mantenere l'integrità delle valutazioni.
 * </p>
 * 
 * @author Sistema Gestione Hackathon
 * @version 1.0
 */
public class Voto {
    /** Identificativo univoco del voto nel database (opzionale) */
    private int id;
    
    /** Identificativo del team valutato (immutabile) */
    private final int idTeam;
    
    /** Identificativo del giudice che ha assegnato il voto (immutabile) */
    private final int idGiudice;
    
    /** Punteggio assegnato, tipicamente da 1 a 10 (immutabile) */
    private final int punteggio;

    /**
     * Costruttore per un nuovo voto.
     * <p>
     * Crea un nuovo oggetto Voto con gli ID del team e del giudice
     * e il punteggio assegnato. L'ID del voto nel database non è
     * specificato in questo costruttore e verrà assegnato dal DAO
     * al momento del salvataggio.
     * </p>
     * 
     * @param idTeam ID del team valutato
     * @param idGiudice ID del giudice che ha assegnato il voto
     * @param punteggio Punteggio assegnato (tipicamente da 1 a 10)
     */
    public Voto(int idTeam, int idGiudice, int punteggio) {
        this.idTeam = idTeam;
        this.idGiudice = idGiudice;
        this.punteggio = punteggio;
    }

    /**
     * Restituisce l'ID del voto nel database.
     * 
     * @return ID del voto
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'ID del voto nel database.
     * <p>
     * Questo metodo è tipicamente utilizzato dal DAO dopo
     * il salvataggio del voto nel database.
     * </p>
     * 
     * @param id Nuovo ID del voto
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Restituisce l'ID del team valutato.
     * 
     * @return ID del team
     */
    public int getIdTeam() {
        return idTeam;
    }

    /**
     * Restituisce l'ID del giudice che ha assegnato il voto.
     * 
     * @return ID del giudice
     */
    public int getIdGiudice() {
        return idGiudice;
    }

    /**
     * Restituisce il punteggio assegnato.
     * 
     * @return Punteggio (tipicamente da 1 a 10)
     */
    public int getPunteggio() {
        return punteggio;
    }

    /**
     * Genera una rappresentazione testuale del voto.
     * 
     * @return Stringa che rappresenta il voto
     */
    @Override
    public String toString() {
        return "Voto{" +
                "id=" + id +
                ", idTeam=" + idTeam +
                ", idGiudice=" + idGiudice +
                ", punteggio=" + punteggio +
                '}';
    }

    /**
     * Verifica se questo voto è uguale a un altro oggetto.
     * <p>
     * Due voti sono considerati uguali se hanno lo stesso team,
     * lo stesso giudice e lo stesso punteggio.
     * </p>
     * 
     * @param o Oggetto da confrontare
     * @return true se i voti sono uguali, false altrimenti
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Voto voto)) return false;
        return idTeam == voto.idTeam &&
                idGiudice == voto.idGiudice &&
                punteggio == voto.punteggio;
    }

    /**
     * Calcola il codice hash per questo voto.
     * <p>
     * Il codice hash è calcolato in base all'ID del team,
     * all'ID del giudice e al punteggio.
     * </p>
     * 
     * @return Codice hash
     */
    @Override
    public int hashCode() {
        int result = idTeam;
        result = 31 * result + idGiudice;
        result = 31 * result + punteggio;
        return result;
    }
}
