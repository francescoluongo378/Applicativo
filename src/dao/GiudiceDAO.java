package dao;

import model.Giudice;
import java.util.List;

public interface GiudiceDAO {
    boolean salva(Giudice giudice);

    Giudice trovaGiudicePerId(int id);

    List<Giudice> findAll();
}
