package postgresdao;

import dao.HackathonDAO;
import model.Hackathon;
import database.ConnessioneDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresHackathonDAO implements HackathonDAO {

    @Override
    public void salvaHackathon(Hackathon hackathon) {
        String sql = "INSERT INTO hackathon (id, titolo, sede, max_partecipanti, max_team) VALUES (?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET titolo = EXCLUDED.titolo, sede = EXCLUDED.sede, max_partecipanti = EXCLUDED.max_partecipanti, max_team = EXCLUDED.max_team";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, hackathon.getId());
            ps.setString(2, hackathon.getTitolo());
            ps.setString(3, hackathon.getSede());
            ps.setInt(4, hackathon.getMaxPartecipanti());
            ps.setInt(5, hackathon.getMaxTeam());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Hackathon> getTuttiHackathon() {
        List<Hackathon> lista = new ArrayList<>();
        String sql = "SELECT id, titolo, sede, max_partecipanti, max_team FROM hackathon";

        try (Connection conn = ConnessioneDatabase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Hackathon h = new Hackathon();
                h.setId(rs.getInt("id"));
                h.setTitolo(rs.getString("titolo"));
                h.setSede(rs.getString("sede"));
                h.setMaxPartecipanti(rs.getInt("max_partecipanti"));
                h.setMaxTeam(rs.getInt("max_team"));
                lista.add(h);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}

