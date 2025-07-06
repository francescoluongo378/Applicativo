package postgresdao;

import dao.GiudiceDAO;
import database.ConnessioneDatabase;
import model.Giudice;
import model.Utente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresGiudiceDAO implements GiudiceDAO {

    @Override
    public boolean salva(Giudice giudice) {
        PostgresUtenteDAO utenteDAO = new PostgresUtenteDAO();
        Utente utenteEsistente = utenteDAO.trovaPerEmail(giudice.getEmail());

        if (utenteEsistente == null) {
            boolean ok = utenteDAO.salva(giudice);
            if (!ok) return false;
            utenteEsistente = utenteDAO.trovaPerEmail(giudice.getEmail());
            if (utenteEsistente == null) return false;
            giudice.setId(utenteEsistente.getId());
        } else {
            giudice.setId(utenteEsistente.getId());
        }

        if (trovaGiudicePerId(giudice.getId()) != null) return true;

        String sql = "INSERT INTO giudice (id_utente) VALUES (?)";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, giudice.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Giudice trovaGiudicePerId(int id) {
        String sql = "SELECT u.id, u.nome, u.email FROM utente u JOIN giudice g ON u.id = g.id_utente WHERE u.id = ?";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Giudice(rs.getInt("id"), rs.getString("nome"), rs.getString("email"));//errore qui
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Giudice> findAllByHackathonId(int hackathonId) {
        List<Giudice> giudici = new ArrayList<>();
        String sql = "SELECT u.id, u.nome, u.email FROM utente u JOIN giudice g ON u.id = g.id_utente";
        
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                giudici.add(new Giudice(rs.getInt("id"), rs.getString("nome"), rs.getString("email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return giudici;
    }

    @Override
    public List<Giudice> findAll() {
        List<Giudice> giudici = new ArrayList<>();
        String sql = "SELECT u.id, u.nome, u.email FROM utente u JOIN giudice g ON u.id = g.id_utente";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                giudici.add(new Giudice(rs.getInt("id"), rs.getString("nome"), rs.getString("email")));//errore qui
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return giudici;
    }


    @Override
    public boolean aggiorna(Giudice giudice) {
        String sql = "UPDATE utente SET nome = ?, email = ? WHERE id = ?";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, giudice.getNome());
            stmt.setString(2, giudice.getEmail());
            stmt.setInt(3, giudice.getId());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean elimina(int id) {
        // Prima eliminiamo il record dalla tabella giudice
        String sqlGiudice = "DELETE FROM giudice WHERE id_utente = ?";
        
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlGiudice)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            
            // Poi eliminiamo il record dalla tabella utente
            String sqlUtente = "DELETE FROM utente WHERE id = ?";
            try (PreparedStatement stmtUtente = conn.prepareStatement(sqlUtente)) {
                stmtUtente.setInt(1, id);
                int rows = stmtUtente.executeUpdate();
                return rows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
