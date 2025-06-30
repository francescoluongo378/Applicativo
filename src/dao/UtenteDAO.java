package dao;

import model.Utente;
import java.util.List;

public interface UtenteDAO {
    boolean salva(Utente utente);
    Utente trovaPerId(int id);
    Utente trovaPerEmail(String email);
    List<Utente> findAll();
    boolean aggiorna(Utente utente);
    boolean elimina(int id);
}
