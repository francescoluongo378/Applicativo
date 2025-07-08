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

            // Salva le date specificate dall'utente
            if (h.getDataInizio() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(h.getDataInizio()));
            } else {
                ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            }
            
            if (h.getDataFine() != null) {
                ps.setTimestamp(6, Timestamp.valueOf(h.getDataFine()));
            } else {
                ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            }
            
            if (h.getInizioIscrizioni() != null) {
                ps.setTimestamp(7, Timestamp.valueOf(h.getInizioIscrizioni()));
            } else {
                ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            }
            
            if (h.getFineIscrizioni() != null) {
                ps.setTimestamp(8, Timestamp.valueOf(h.getFineIscrizioni()));
            } else {
                ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            }

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
                
                // Recupera le date
                Timestamp dataInizio = rs.getTimestamp("data_inizio");
                if (dataInizio != null) {
                    h.setDataInizio(dataInizio.toLocalDateTime());
                }
                
                Timestamp dataFine = rs.getTimestamp("data_fine");
                if (dataFine != null) {
                    h.setDataFine(dataFine.toLocalDateTime());
                }
                
                Timestamp inizioIscrizioni = rs.getTimestamp("inizio_iscrizioni");
                if (inizioIscrizioni != null) {
                    h.setInizioIscrizioni(inizioIscrizioni.toLocalDateTime());
                }
                
                Timestamp fineIscrizioni = rs.getTimestamp("fine_iscrizioni");
                if (fineIscrizioni != null) {
                    h.setFineIscrizioni(fineIscrizioni.toLocalDateTime());
                }

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
                
                // Recupera le date
                Timestamp dataInizio = rs.getTimestamp("data_inizio");
                if (dataInizio != null) {
                    h.setDataInizio(dataInizio.toLocalDateTime());
                }
                
                Timestamp dataFine = rs.getTimestamp("data_fine");
                if (dataFine != null) {
                    h.setDataFine(dataFine.toLocalDateTime());
                }
                
                Timestamp inizioIscrizioni = rs.getTimestamp("inizio_iscrizioni");
                if (inizioIscrizioni != null) {
                    h.setInizioIscrizioni(inizioIscrizioni.toLocalDateTime());
                }
                
                Timestamp fineIscrizioni = rs.getTimestamp("fine_iscrizioni");
                if (fineIscrizioni != null) {
                    h.setFineIscrizioni(fineIscrizioni.toLocalDateTime());
                }

                result.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return result;
    }

    @Override
    public boolean aggiorna(Hackathon h) {
        if (h.getId() <= 0) return false;
        
        String sql = "UPDATE hackathon SET titolo = ?, sede = ?, max_partecipanti = ?, max_team = ?, "
                + "data_inizio = ?, data_fine = ?, inizio_iscrizioni = ?, fine_iscrizioni = ? "
                + "WHERE id = ?";
        
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, h.getTitolo());
            ps.setString(2, h.getSede());
            ps.setInt(3, h.getMaxPartecipanti());
            ps.setInt(4, h.getMaxTeam());
            
            // Aggiorna le date
            if (h.getDataInizio() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(h.getDataInizio()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }
            
            if (h.getDataFine() != null) {
                ps.setTimestamp(6, Timestamp.valueOf(h.getDataFine()));
            } else {
                ps.setNull(6, Types.TIMESTAMP);
            }
            
            if (h.getInizioIscrizioni() != null) {
                ps.setTimestamp(7, Timestamp.valueOf(h.getInizioIscrizioni()));
            } else {
                ps.setNull(7, Types.TIMESTAMP);
            }
            
            if (h.getFineIscrizioni() != null) {
                ps.setTimestamp(8, Timestamp.valueOf(h.getFineIscrizioni()));
            } else {
                ps.setNull(8, Types.TIMESTAMP);
            }
            
            ps.setInt(9, h.getId());
            
            int affected = ps.executeUpdate();
            return affected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean elimina(int id) {
        return false;
    }
}

