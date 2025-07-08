package postgresdao;

import dao.DocumentoDAO;
import database.ConnessioneDatabase;
import model.Documento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresDocumentoDAO implements DocumentoDAO {

    public PostgresDocumentoDAO() {

    }

    @Override
    public boolean salvaDocumento(Documento documento) {
        String sql = "INSERT INTO documento (titolo, id_team) VALUES (?, ?)";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, documento.getTitolo());
            stmt.setInt(2, documento.getIdTeam());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Errore durante il salvataggio del documento: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Documento> findByTeam(int idTeam) {
        List<Documento> documenti = new ArrayList<>();
        String sql = "SELECT * FROM documento WHERE id_team = ?";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTeam);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Documento documento = new Documento();
                documento.setId(rs.getInt("id"));
                documento.setTitolo(rs.getString("titolo"));
                documento.setIdTeam(rs.getInt("id_team"));
                documenti.add(documento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Errore durante il recupero dei documenti per team: " + e.getMessage());
        }

        return documenti;
    }
    
    @Override
    public boolean aggiorna(Documento documento) {
        String sql = "UPDATE documento SET titolo = ?, id_team = ? WHERE id = ?";
        
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, documento.getTitolo());
            stmt.setInt(2, documento.getIdTeam());
            stmt.setInt(3, documento.getId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Errore durante l'aggiornamento del documento: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean elimina(int id) {
        String sql = "DELETE FROM documento WHERE id = ?";
        
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Errore durante l'eliminazione del documento: " + e.getMessage());
            return false;
        }
    }
}

