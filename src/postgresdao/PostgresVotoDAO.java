package postgresdao;

import dao.VotoDAO;
import database.ConnessioneDatabase;
import model.Voto;

import java.sql.*;


public class PostgresVotoDAO implements VotoDAO {

    /**
     * Salva un nuovo Voto nel database PostgreSQL.
     * <p>
     * Questo metodo delega al metodo {@link #salvaVoto(int, int, int)}
     * passando i singoli attributi del voto.
     * </p>
     * 
     * @param voto Voto da salvare
     * @return true se il salvataggio è riuscito, false altrimenti
     */
    @Override
    public boolean salvaVoto(Voto voto) {
        return salvaVoto(voto.getIdGiudice(), voto.getIdTeam(), voto.getPunteggio());
    }

    /**
     * Salva un nuovo Voto nel database PostgreSQL specificando i singoli attributi.
     * <p>
     * Questo metodo verifica prima se esiste già un voto dello stesso giudice
     * per lo stesso team. Se esiste, aggiorna il voto esistente, altrimenti
     * inserisce un nuovo voto.
     * </p>
     * 
     * @param idGiudice ID del giudice che ha assegnato il voto
     * @param idTeam ID del team valutato
     * @param punteggio Punteggio assegnato (tipicamente da 1 a 10)
     * @return true se il salvataggio è riuscito, false altrimenti
     */
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
                    System.out.println("Voto aggiornato con ID: " + votoId + ", righe modificate: " + rowsUpdated);
                    return rowsUpdated > 0;
                }
            } else {
                // Non esiste un voto, quindi facciamo un INSERT
                String insertSql = "INSERT INTO voto (id_giudice, id_team, punteggio) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, idGiudice);
                    insertStmt.setInt(2, idTeam);
                    insertStmt.setInt(3, punteggio);
                    
                    // Stampa i valori che stiamo per inserire
                    System.out.println("Inserimento voto: id_giudice=" + idGiudice + 
                                      ", id_team=" + idTeam + ", punteggio=" + punteggio);
                    
                    int rowsInserted = insertStmt.executeUpdate();
                    System.out.println("Nuovo voto inserito, righe inserite: " + rowsInserted);
                    return rowsInserted > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il salvataggio del voto: " + e.getMessage());
            e.printStackTrace();
            
            // Verifichiamo se il problema è legato a vincoli di chiave esterna
            if (e.getMessage().contains("foreign key") || e.getMessage().contains("violates foreign key")) {
                System.err.println("Errore di chiave esterna. Verifica che il giudice (ID: " + idGiudice + 
                                  ") e il team (ID: " + idTeam + ") esistano nel database.");
                
                // Verifica se il giudice esiste
                try (Connection conn = ConnessioneDatabase.getInstance().getConnection()) {
                    String checkGiudice = "SELECT id_utente FROM giudice WHERE id_utente = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(checkGiudice)) {
                        stmt.setInt(1, idGiudice);
                        ResultSet rs = stmt.executeQuery();
                        if (!rs.next()) {
                            System.err.println("Il giudice con ID " + idGiudice + " non esiste nella tabella giudice.");
                        }
                    }
                    
                    // Verifica se il team esiste
                    String checkTeam = "SELECT id FROM team WHERE id = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(checkTeam)) {
                        stmt.setInt(1, idTeam);
                        ResultSet rs = stmt.executeQuery();
                        if (!rs.next()) {
                            System.err.println("Il team con ID " + idTeam + " non esiste nella tabella team.");
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            
            return false;
        }
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
