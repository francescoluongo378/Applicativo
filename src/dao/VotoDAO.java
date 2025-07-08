package dao;

import model.Voto;

/**
 * Interfaccia per l'accesso ai dati dei Voti.
 * <p>
 * Questa interfaccia definisce i metodi per l'accesso ai dati dei Voti
 * nel database. Le implementazioni concrete di questa interfaccia
 * forniscono l'accesso a database specifici (es. PostgreSQL).
 * </p>
 * 
 * @author Sistema Gestione Hackathon
 * @version 1.0
 */
public interface VotoDAO {
    /**
     * Salva un nuovo Voto nel database.
     * 
     * @param voto Voto da salvare
     * @return true se il salvataggio è riuscito, false altrimenti
     */
    boolean salvaVoto(Voto voto);
    
    /**
     * Salva un nuovo Voto nel database specificando i singoli attributi.
     * <p>
     * Questo metodo è una convenienza per creare e salvare un voto
     * senza dover creare prima un oggetto Voto.
     * </p>
     * 
     * @param idGiudice ID del giudice che ha assegnato il voto
     * @param idTeam ID del team valutato
     * @param punteggio Punteggio assegnato (tipicamente da 1 a 10)
     * @return true se il salvataggio è riuscito, false altrimenti
     */
    boolean salvaVoto(int idGiudice, int idTeam, int punteggio);
    
    /**
     * Aggiorna un Voto esistente.
     * 
     * @param voto Voto da aggiornare
     * @return true se l'aggiornamento è riuscito, false altrimenti
     */
    boolean aggiorna(Voto voto);
    
    /**
     * Elimina un Voto dal database.
     * 
     * @param id ID del Voto da eliminare
     * @return true se l'eliminazione è riuscita, false altrimenti
     */
    boolean elimina(int id);
}

