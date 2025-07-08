package dao;
import model.*;

import java.util.List;


public interface PartecipanteDAO {
    /**
     * Salva un nuovo Partecipante nel database.
     * 
     * @param p Partecipante da salvare
     * @return true se il salvataggio è riuscito, false altrimenti
     */
    boolean salva(Partecipante p);
    
    /**
     * Salva un nuovo Partecipante nel database.
     * <p>
     * Questo metodo è simile a {@link #salva(Partecipante)} ma non restituisce
     * un valore booleano.
     * </p>
     * 
     * @param p Partecipante da salvare
     */
    void salvaPartecipante(Partecipante p);
    
    /**
     * Trova un Partecipante per ID (stringa).
     * 
     * @param id ID del Partecipante da cercare (come stringa)
     * @return Partecipante trovato, null se non esiste
     */
    Partecipante trovaPartecipantePerId(String id);
    
    /**
     * Trova un Partecipante per ID (intero).
     * 
     * @param id ID del Partecipante da cercare
     * @return Partecipante trovato, null se non esiste
     */
    Partecipante findById(int id);

    /**
     * Trova tutti i Partecipanti nel database.
     * 
     * @return Lista di tutti i Partecipanti
     */
    List<Partecipante> findAll();

    /**
     * Aggiorna un Partecipante esistente.
     * 
     * @param p Partecipante da aggiornare
     * @return true se l'aggiornamento è riuscito, false altrimenti
     */
    boolean aggiorna(Partecipante p);

    /**
     * Associa un Partecipante a un Team.
     * <p>
     * Questo metodo aggiorna il campo teamId del Partecipante nel database.
     * </p>
     * 
     * @param idPartecipante ID del Partecipante
     * @param idTeam ID del Team
     */
    void salvaPartecipanteNelTeam(int idPartecipante, int idTeam);
    
    /**
     * Elimina un Partecipante dal database.
     * 
     * @param id ID del Partecipante da eliminare
     * @return true se l'eliminazione è riuscita, false altrimenti
     */
    boolean elimina(int id);
}