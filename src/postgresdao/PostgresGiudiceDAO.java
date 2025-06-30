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
        Utente u = giudice;
        PostgresUtenteDAO utenteDAO = new PostgresUtenteDAO();
        Utente utenteEsistente = utenteDAO.trovaPerEmail(u.getEmail());

        if (utenteEsistente == null) {
            boolean ok = utenteDAO.salva(u);
            if (!ok) return false;
            utenteEsistente = utenteDAO.trovaPerEmail(u.getEmail());
            if (utenteEsistente == null) return false;
            u.setId(utenteEsistente.getId());
        } else {
            u.setId(utenteEsistente.getId());
        }

        if (trovaGiudicePerId(u.getId()) != null) return true;

        String sql = "INSERT INTO giudice (id_utente) VALUES (?)";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, u.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Giudice trovaGiudicePerId(int id) {
        String sql = "SELECT u.id, u.nome, u.email FROM utente u JOIN giudice g ON u.id = g.id_utente WHERE u.id = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
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
    public List<Giudice> findAll() {
        List<Giudice> giudici = new ArrayList<>();
        String sql = "SELECT u.id, u.nome, u.email FROM utente u JOIN giudice g ON u.id = g.id_utente";

        try (Connection conn = ConnessioneDatabase.getConnection();
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


    public boolean aggiornaGiudice(Giudice giudice) {
        String sql = "UPDATE utente SET nome = ?, email = ? WHERE id = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
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
}
