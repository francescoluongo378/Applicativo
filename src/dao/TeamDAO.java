package dao;
import model.*;

import java.util.List;

/**
 * Interfaccia per l'accesso ai dati dei Team.
 * <p>
 * Questa interfaccia definisce i metodi per l'accesso ai dati dei Team
 * nel database. Le implementazioni concrete di questa interfaccia
 * forniscono l'accesso a database specifici (es. PostgreSQL).
 * </p>
 * 
 * @author Sistema Gestione Hackathon
 * @version 1.0
 */
public interface TeamDAO {
    /**
     * Salva un nuovo Team nel database.
     * 
     * @param team Team da salvare
     * @return Team salvato con ID assegnato, null in caso di errore
     */
    Team salva(Team team);

    /**
     * Trova tutti i Team di un Hackathon.
     * 
     * @param hackathonId ID dell'Hackathon
     * @return Lista di Team dell'Hackathon
     */
    List<Team> findAllByHackathonId(int hackathonId);
    
    /**
     * Aggiorna un Team esistente.
     * 
     * @param team Team da aggiornare
     * @return true se l'aggiornamento è riuscito, false altrimenti
     */
    boolean aggiorna(Team team);
    
    /**
     * Elimina un Team dal database.
     * 
     * @param id ID del Team da eliminare
     * @return true se l'eliminazione è riuscita, false altrimenti
     */
    boolean elimina(int id);

    /**
     * Trova un Team per nome.
     * 
     * @param nome Nome del Team da cercare
     * @return Team trovato, null se non esiste
     */
    Team trovaTeamPerNome(String nome);
    
    /**
     * Trova un Team per ID.
     * 
     * @param id ID del Team da cercare
     * @return Team trovato, null se non esiste
     */
    Team trovaPerId(int id);
    
    /**
     * Trova tutti i Team nel database.
     * 
     * @return Lista di tutti i Team
     */
    List<Team> findAll();
}

