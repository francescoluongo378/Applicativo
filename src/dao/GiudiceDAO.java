package dao;

import model.Giudice;
import java.util.List;

public interface GiudiceDAO {
    boolean salva(Giudice giudice);
    Giudice trovaGiudicePerId(int id);
    List<Giudice> findAllByHackathonId(int hackathonId);
    List<Giudice> findAll();
    boolean aggiorna(Giudice giudice);
    boolean elimina(int id);
}
