package dao;
import model.*;

import java.util.List;
public interface PartecipanteDAO {
    boolean salva(Partecipante p);
    void salvaPartecipante(Partecipante p);
    Partecipante trovaPartecipantePerId(String id);
    Partecipante findById(int id);

    List<Partecipante> findAll();

    boolean aggiorna(Partecipante p);

    void salvaPartecipanteNelTeam(int idPartecipante, int idTeam);
    boolean elimina(int id);
}