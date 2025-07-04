package postgresdao;

import dao.VotoDAO;
import database.ConnessioneDatabase;
import model.Voto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresVotoDAO implements VotoDAO {

    @Override
    public boolean salvaVoto(Voto voto) {
        return salvaVoto(voto.getIdGiudice(), voto.getIdTeam(), voto.getPunteggio());
    }

    @Override
    public boolean salvaVoto(int idGiudice, int idTeam, int punteggio) {
        // Prima verifichiamo se esiste già un voto di questo giudice per questo team
        String checkSql = "SELECT id FROM voto WHERE id_giudice = ? AND id_team = ?";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            
            checkStmt.setInt(1, idGiudice);
            checkStmt.setInt(2, idTeam);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                // Esiste già un voto, quindi facciamo un UPDATE
                int votoId = rs.getInt("id");
                String updateSql = "UPDATE voto SET punteggio = ? WHERE id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, punteggio);
                    updateStmt.setInt(2, votoId);
                    int rowsUpdated = updateStmt.executeUpdate();
                    return rowsUpdated > 0;
                }
            } else {
                // Non esiste un voto, quindi facciamo un INSERT
                String insertSql = "INSERT INTO voto (id_giudice, id_team, punteggio) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, idGiudice);
                    insertStmt.setInt(2, idTeam);
                    insertStmt.setInt(3, punteggio);
                    int rowsInserted = insertStmt.executeUpdate();
                    return rowsInserted > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il salvataggio del voto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Voto> listaVoti() {
        List<Voto> voti = new ArrayList<>();
        String sql = "SELECT id_giudice, id_team, punteggio FROM voto";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idGiudice = rs.getInt("id_giudice");
                int idTeam = rs.getInt("id_team");
                int punteggio = rs.getInt("punteggio");
                voti.add(new Voto(idGiudice, idTeam, punteggio));
            }

        } catch (SQLException e) {
            System.err.println("Errore nella lettura dei voti: " + e.getMessage());
        }

        return voti;
    }

    @Override
    public List<Voto> listaVotiPerTeam(int idTeam) {
        List<Voto> voti = new ArrayList<>();
        String query = "SELECT id_team, id_giudice, punteggio FROM voto WHERE id_team = ?";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idTeam);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int teamId = rs.getInt("id_team");
                int giudiceId = rs.getInt("id_giudice");
                int punteggio = rs.getInt("punteggio");

                Voto voto = new Voto(giudiceId, teamId, punteggio);
                voti.add(voto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return voti;
    }
    
    @Override
    public boolean aggiorna(Voto voto) {
        // Utilizziamo il metodo salvaVoto che già gestisce l'aggiornamento se il voto esiste
        return salvaVoto(voto);
    }
    
    @Override
    public boolean elimina(int id) {
        String sql = "DELETE FROM voto WHERE id = ?";
        
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Errore durante l'eliminazione del voto: " + e.getMessage());
            return false;
        }
    }
}
