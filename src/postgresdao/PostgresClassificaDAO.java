package postgresdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Classifica;
import model.Team;
import dao.ClassificaDAO;
import database.ConnessioneDatabase;

public class PostgresClassificaDAO implements ClassificaDAO {

    @Override
    public Classifica getClassifica(int hackathonId) {
        Classifica classifica = new Classifica();

        String sql = "SELECT t.id, t.nome, SUM(v.punteggio) AS totale "
                + "FROM team t JOIN voto v ON t.id = v.id_team "
                + "WHERE t.hackathon_id = ? "
                + "GROUP BY t.id, t.nome ORDER BY totale DESC";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, hackathonId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                int totale = rs.getInt("totale");

                System.out.println("DEBUG Team: " + nome + " ID: " + id + " Punti: " + totale);

                Team team = new Team(id, nome, hackathonId);
                team.setPuntiTotali(totale);
                classifica.aggiungiTeam(team);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classifica;
    }

    @Override
    public Classifica getClassifica() {
        Classifica classifica = new Classifica();

        String sql = "SELECT t.id, t.nome, t.hackathon_id, SUM(v.punteggio) AS totale "
                + "FROM team t JOIN voto v ON t.id = v.id_team "
                + "GROUP BY t.id, t.nome, t.hackathon_id ORDER BY totale DESC";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                int hackId = rs.getInt("hackathon_id");
                int totale = rs.getInt("totale");

                System.out.println("DEBUG Team: " + nome + " ID: " + id + " Hackathon: " + hackId + " Punti: " + totale);

                Team team = new Team(id, nome, hackId);
                team.setPuntiTotali(totale);
                classifica.aggiungiTeam(team);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classifica;
    }
}
