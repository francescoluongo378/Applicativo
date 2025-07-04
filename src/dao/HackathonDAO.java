package dao;

import model.Hackathon;
import java.util.List;

public interface HackathonDAO {
    boolean salva(Hackathon h);
    Hackathon trovaPerId(int id);
    List<Hackathon> findAll();
    boolean aggiorna(Hackathon h);
    boolean elimina(int id);
}

