package dao;

import model.Classifica;

/**
 * Interfaccia per l'accesso ai dati della Classifica.
 * <p>
 * Questa interfaccia definisce i metodi per l'accesso ai dati della Classifica
 * nel database. Le implementazioni concrete di questa interfaccia
 * forniscono l'accesso a database specifici (es. PostgreSQL).
 * </p>
 * <p>
 * A differenza delle altre interfacce DAO, questa non fornisce metodi CRUD
 * completi perché la Classifica è un oggetto derivato dai Team e dai Voti,
 * non un'entità persistente autonoma.
 * </p>
 * 
 * @author Sistema Gestione Hackathon
 * @version 1.0
 */
public interface ClassificaDAO {
    /**
     * Ottiene la classifica per un Hackathon.
     * <p>
     * Questo metodo recupera tutti i team dell'Hackathon specificato,
     * li ordina in base ai voti ricevuti e restituisce un oggetto Classifica
     * contenente la lista ordinata.
     * </p>
     * 
     * @param hackathonId ID dell'Hackathon
     * @return Oggetto Classifica contenente i team ordinati per punteggio
     */
    Classifica getClassifica(int hackathonId);
}
