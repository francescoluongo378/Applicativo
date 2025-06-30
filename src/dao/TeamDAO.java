package dao;
import model.*;
import postgresdao.*;
import java.util.List;

public interface TeamDAO {
    boolean aggiungi(Team team);
    Team findById(int id);
    List<Team> findAll();
    boolean aggiorna(Team team);
    boolean elimina(int id);

    // === Questi servono al Controller ===
    boolean salva(Team team);
    boolean aggiornaProgresso(String nomeTeam, int progresso);
    Team trovaTeamPerNome(String nome);

    Team findById(String idTeam);
}
