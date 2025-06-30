package dao;
import model.*;

import java.util.List;
public interface PartecipanteDAO {
    boolean salvaPartecipante(Partecipante p);

    // Trova un partecipante per id (id passato come stringa)
    Partecipante trovaPartecipantePerId(String id);

    List<Partecipante> findAll();

    Partecipante findById(int id);

    boolean aggiorna(Partecipante p);

    // Lista partecipanti, delega a findAll()
    List<Partecipante> listaPartecipanti();

    // Aggiorna un partecipante esistente
    void aggiornaPartecipante(Partecipante partecipante);


    void salvaPartecipanteNelTeam(String nomePartecipante, int id);

    boolean salva(Partecipante p);
}