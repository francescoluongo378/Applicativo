package postgresdao;

import dao.HackathonDAO;
import model.Hackathon;
import database.ConnessioneDatabase;

import java.sql.*;
import java.util.List;

public class PostgresHackathonDAO implements HackathonDAO {
    @Override
    public boolean salva(Hackathon h) {
        String sql = "INSERT INTO hackathon "
                + "(titolo, sede, max_partecipanti, max_team, data_inizio, data_fine, inizio_iscrizioni, fine_iscrizioni) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, h.getTitolo());
            ps.setString(2, h.getSede());
            ps.setInt(3, h.getMaxPartecipanti());
            ps.setInt(4, h.getMaxTeam());

            // Usa la data odierna per tutti i campi obbligatori
            java.sql.Date oggi = new java.sql.Date(System.currentTimeMillis());
            ps.setDate(5, oggi); // data_inizio
            ps.setDate(6, oggi); // data_fine
            ps.setDate(7, oggi); // inizio_iscrizioni
            ps.setDate(8, oggi); // fine_iscrizioni

            int affected = ps.executeUpdate();
            if (affected == 0) return false;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    h.setId(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Hackathon trovaPerId(int id) {
        String sql = "SELECT * FROM hackathon WHERE id = ?";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Hackathon h = new Hackathon();
                h.setId(rs.getInt("id"));
                h.setTitolo(rs.getString("titolo"));
                h.setSede(rs.getString("sede"));
                h.setMaxPartecipanti(rs.getInt("max_partecipanti"));
                h.setMaxTeam(rs.getInt("max_team"));
                
                // Gestione delle date se necessario
                // h.setDataInizio(rs.getTimestamp("data_inizio").toLocalDateTime());
                // h.setDataFine(rs.getTimestamp("data_fine").toLocalDateTime());
                // h.setInizioIscrizioni(rs.getTimestamp("inizio_iscrizioni").toLocalDateTime());
                // h.setFineIscrizioni(rs.getTimestamp("fine_iscrizioni").toLocalDateTime());
                
                return h;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Hackathon> findAll() {
        String sql = "SELECT * FROM hackathon";
        List<Hackathon> result = new java.util.ArrayList<>();
        
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Hackathon h = new Hackathon();
                h.setId(rs.getInt("id"));
                h.setTitolo(rs.getString("titolo"));
                h.setSede(rs.getString("sede"));
                h.setMaxPartecipanti(rs.getInt("max_partecipanti"));
                h.setMaxTeam(rs.getInt("max_team"));
                
                // Gestione delle date se necessario
                // h.setDataInizio(rs.getTimestamp("data_inizio").toLocalDateTime());
                // h.setDataFine(rs.getTimestamp("data_fine").toLocalDateTime());
                // h.setInizioIscrizioni(rs.getTimestamp("inizio_iscrizioni").toLocalDateTime());
                // h.setFineIscrizioni(rs.getTimestamp("fine_iscrizioni").toLocalDateTime());
                
                result.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return result;
    }

    @Override
    public boolean aggiorna(Hackathon h) {
        return false;
    }

    @Override
    public boolean elimina(int id) {
        return false;
    }
}

