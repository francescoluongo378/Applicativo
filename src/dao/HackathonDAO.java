package dao;

import model.Hackathon;
import java.util.List;

public interface HackathonDAO {
    void salvaHackathon(Hackathon hackathon);
    List<Hackathon> getTuttiHackathon();
}

