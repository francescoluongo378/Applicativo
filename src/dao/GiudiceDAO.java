package dao;

import model.Giudice;
import java.util.List;

/**
 * Interfaccia per l'accesso ai dati dei Giudici.
 * <p>
 * Questa interfaccia definisce i metodi per l'accesso ai dati dei Giudici
 * nel database. Le implementazioni concrete di questa interfaccia
 * forniscono l'accesso a database specifici (es. PostgreSQL).
 * </p>
 * 
 * @author Sistema Gestione Hackathon
 * @version 1.0
 */
public interface GiudiceDAO {
    /**
     * Salva un nuovo Giudice nel database.
     * 
     * @param giudice Giudice da salvare
     * @return true se il salvataggio è riuscito, false altrimenti
     */
    boolean salva(Giudice giudice);
    
    /**
     * Trova un Giudice per ID.
     * 
     * @param id ID del Giudice da cercare
     * @return Giudice trovato, null se non esiste
     */
    Giudice trovaGiudicePerId(int id);
    
    /**
     * Trova tutti i Giudici di un Hackathon.
     * 
     * @param hackathonId ID dell'Hackathon
     * @return Lista di Giudici dell'Hackathon
     */
    List<Giudice> findAllByHackathonId(int hackathonId);
    
    /**
     * Trova tutti i Giudici nel database.
     * 
     * @return Lista di tutti i Giudici
     */
    List<Giudice> findAll();
    
    /**
     * Aggiorna un Giudice esistente.
     * 
     * @param giudice Giudice da aggiornare
     * @return true se l'aggiornamento è riuscito, false altrimenti
     */
    boolean aggiorna(Giudice giudice);
    
    /**
     * Elimina un Giudice dal database.
     * 
     * @param id ID del Giudice da eliminare
     * @return true se l'eliminazione è riuscita, false altrimenti
     */
    boolean elimina(int id);
}
