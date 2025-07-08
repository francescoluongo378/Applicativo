package dao;

import model.Classifica;


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
