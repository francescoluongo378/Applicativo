package dao;
import model.*;

import java.util.List;

public interface TeamDAO {
    Team salva(Team team); // deve ritornare Team

    List<Team> findAllByHackathonId(int hackathonId);
    boolean aggiorna(Team team);
    boolean elimina(int id);
    boolean aggiornaProgresso(int idTeam, int nuovoProgresso);
    boolean aggiornaProgresso(String nomeTeam, int progresso);

    Team trovaTeamPerNome(String nome);
    Team trovaPerId(int id);
    List<Team> findAll();
}

