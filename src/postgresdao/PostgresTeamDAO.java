package postgresdao;

import dao.TeamDAO;
import database.ConnessioneDatabase;
import model.Hackathon;
import model.Team;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresTeamDAO implements TeamDAO {

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
    public boolean aggiornaProgresso(int idTeam, int nuovoProgresso) {
        String sql = "UPDATE team SET progresso = ? WHERE id = ?";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nuovoProgresso);
            stmt.setInt(2, idTeam);

            int righe = stmt.executeUpdate();
            return righe > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
    public boolean aggiornaProgresso(String nomeTeam, int progresso) {
        String sql = "UPDATE team SET progresso = ? WHERE nome = ?";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, progresso);
            stmt.setString(2, nomeTeam);

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
