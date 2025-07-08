package postgresdao;

import dao.TeamDAO;
import database.ConnessioneDatabase;
import model.Hackathon;
import model.Team;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione PostgreSQL dell'interfaccia TeamDAO.
 * <p>
 * Questa classe fornisce l'accesso ai dati dei Team nel database PostgreSQL.
 * Implementa tutti i metodi definiti nell'interfaccia TeamDAO utilizzando
 * query SQL specifiche per PostgreSQL.
 * </p>
 * 
 * @author Sistema Gestione Hackathon
 * @version 1.0
 */
public class PostgresTeamDAO implements TeamDAO {

    /**
     * Salva un nuovo Team nel database PostgreSQL.
     * <p>
     * Questo metodo inserisce un nuovo record nella tabella team
     * e restituisce il Team con l'ID assegnato dal database.
     * </p>
     * 
     * @param team Team da salvare
     * @return Team salvato con ID assegnato, null in caso di errore
     */
    @Override
    public Team salva(Team team) {
        if (team == null) return null;

        String sql = "INSERT INTO team (nome, descrizione, progresso, hackathon_id) VALUES (?, ?, ?, ?) RETURNING id";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, team.getNome());
            stmt.setString(2, team.getDescrizione());
            stmt.setInt(3, team.getProgresso());
            stmt.setInt(4, team.getHackathon().getId());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                team.setId(rs.getInt("id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return team;
    }

    @Override
    public List<Team> findAllByHackathonId(int hackathonId) {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT * FROM team WHERE hackathon_id = ?";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, hackathonId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Team team = new Team();
                team.setId(rs.getInt("id"));
                team.setNome(rs.getString("nome"));
                team.setDescrizione(rs.getString("descrizione"));
                team.setProgresso(rs.getInt("progresso"));

                Hackathon h = new Hackathon();
                h.setId(rs.getInt("hackathon_id"));
                team.setHackathon(h);

                teams.add(team);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teams;
    }

    @Override
    public boolean aggiorna(Team team) {
        String sql = "UPDATE team SET nome = ?, descrizione = ?, progresso = ?, hackathon_id = ? WHERE id = ?";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, team.getNome());
            stmt.setString(2, team.getDescrizione());
            stmt.setInt(3, team.getProgresso());
            stmt.setInt(4, team.getHackathon().getId());
            stmt.setInt(5, team.getId());

            int righe = stmt.executeUpdate();
            return righe > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean elimina(int id) {
        String sql = "DELETE FROM team WHERE id = ?";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int righe = stmt.executeUpdate();
            return righe > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public Team trovaTeamPerNome(String nome) {
        String sql = "SELECT * FROM team WHERE nome = ?";
        Team team = null;

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                team = new Team();
                team.setId(rs.getInt("id"));
                team.setNome(rs.getString("nome"));
                team.setDescrizione(rs.getString("descrizione"));
                team.setProgresso(rs.getInt("progresso"));

                Hackathon h = new Hackathon();
                h.setId(rs.getInt("hackathon_id"));
                team.setHackathon(h);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return team;
    }

    @Override
    public Team trovaPerId(int id) {
        String sql = "SELECT * FROM team WHERE id = ?";
        Team team = null;

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                team = new Team();
                team.setId(rs.getInt("id"));
                team.setNome(rs.getString("nome"));
                team.setDescrizione(rs.getString("descrizione"));
                team.setProgresso(rs.getInt("progresso"));

                Hackathon h = new Hackathon();
                h.setId(rs.getInt("hackathon_id"));
                team.setHackathon(h);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return team;
    }

    @Override
    public List<Team> findAll() {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT * FROM team";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Team team = new Team();
                team.setId(rs.getInt("id"));
                team.setNome(rs.getString("nome"));
                team.setDescrizione(rs.getString("descrizione"));
                team.setProgresso(rs.getInt("progresso"));

                Hackathon h = new Hackathon();
                h.setId(rs.getInt("hackathon_id"));
                team.setHackathon(h);

                teams.add(team);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teams;
    }
}
