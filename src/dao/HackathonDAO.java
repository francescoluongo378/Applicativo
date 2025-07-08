package dao;

import model.Hackathon;
import java.util.List;


public interface HackathonDAO {
    /**
     * Salva un nuovo Hackathon nel database.
     * 
     * @param h Hackathon da salvare
     * @return true se il salvataggio è riuscito, false altrimenti
     */
    boolean salva(Hackathon h);
    
    /**
     * Trova un Hackathon per ID.
     * 
     * @param id ID dell'Hackathon da cercare
     * @return Hackathon trovato, null se non esiste
     */
    Hackathon trovaPerId(int id);
    
    /**
     * Trova tutti gli Hackathon nel database.
     * 
     * @return Lista di tutti gli Hackathon
     */
    List<Hackathon> findAll();
    
    /**
     * Aggiorna un Hackathon esistente.
     * 
     * @param h Hackathon da aggiornare
     * @return true se l'aggiornamento è riuscito, false altrimenti
     */
    boolean aggiorna(Hackathon h);
    
    /**
     * Elimina un Hackathon dal database.
     * 
     * @param id ID dell'Hackathon da eliminare
     * @return true se l'eliminazione è riuscita, false altrimenti
     */
    boolean elimina(int id);
}

