package postgresdao;

import dao.TeamDAO;
import model.Team;
import database.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresTeamDAO implements TeamDAO {

    @Override
    public boolean aggiungi(Team team) {
        String sql = "INSERT INTO team (nome, descrizione, progresso) VALUES (?, ?, ?)";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, team.getNome());
            stmt.setString(2, team.getDescrizione());
            stmt.setInt(3, team.getProgresso());

            int rows = stmt.executeUpdate();
            if (rows == 0) return false;

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    team.setId(generatedKeys.getInt(1));
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Team findById(int id) {
        String sql = "SELECT * FROM team WHERE id = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Team team = new Team();
                    team.setId(rs.getInt("id"));
                    team.setNome(rs.getString("nome"));
                    team.setDescrizione(rs.getString("descrizione"));
                    team.setProgresso(rs.getInt("progresso"));
                    return team;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Team> findAll() {
        List<Team> lista = new ArrayList<>();
        String sql = "SELECT * FROM team";
        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Team team = new Team();
                team.setId(rs.getInt("id"));
                team.setNome(rs.getString("nome"));
                team.setDescrizione(rs.getString("descrizione"));
                team.setProgresso(rs.getInt("progresso"));
                lista.add(team);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean aggiorna(Team team) {
        String sql = "UPDATE team SET nome = ?, descrizione = ?, progresso = ? WHERE id = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, team.getNome());
            stmt.setString(2, team.getDescrizione());
            stmt.setInt(3, team.getProgresso());
            stmt.setInt(4, team.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean elimina(int id) {
        String sql = "DELETE FROM team WHERE id = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean salva(Team team) {
        return false;
    }

    @Override
    public boolean aggiornaProgresso(String nomeTeam, int progresso) {
        return false;
    }

    @Override
    public Team trovaTeamPerNome(String nome) {
        return null;
    }

    @Override
    public Team findById(String idTeam) {
        return null;
    }
}
