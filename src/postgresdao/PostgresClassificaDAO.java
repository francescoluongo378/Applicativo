package postgresdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.*;
import dao.ClassificaDAO;
import database.ConnessioneDatabase;

/**
 * Implementazione PostgreSQL dell'interfaccia ClassificaDAO.
 * <p>
 * Questa classe fornisce l'accesso ai dati della Classifica nel database PostgreSQL.
 * Implementa il metodo definito nell'interfaccia ClassificaDAO utilizzando
 * query SQL specifiche per PostgreSQL.
 * </p>
 * 
 * @author Sistema Gestione Hackathon
 * @version 1.0
 */
public class PostgresClassificaDAO implements ClassificaDAO {

    /**
     * Ottiene la classifica per un Hackathon dal database PostgreSQL.
     * <p>
     * Questo metodo esegue una query SQL che:
     * <ol>
     *   <li>Recupera tutti i team dell'Hackathon specificato</li>
     *   <li>Calcola la media dei voti per ogni team</li>
     *   <li>Ordina i team per media decrescente</li>
     *   <li>Carica i voti effettivi per ogni team</li>
     *   <li>Crea un oggetto Classifica contenente i team ordinati</li>
     * </ol>
     * </p>
     * 
     * @param hackathonId ID dell'Hackathon
     * @return Oggetto Classifica contenente i team ordinati per punteggio
     */
    @Override
    public Classifica getClassifica(int hackathonId) {
        Classifica classifica = new Classifica();

        String sql = "SELECT t.id, t.nome, AVG(v.punteggio) AS media, COUNT(v.id) AS num_voti "
                + "FROM team t JOIN voto v ON t.id = v.id_team "
                + "WHERE t.hackathon_id = ? "
                + "GROUP BY t.id, t.nome "
                + "ORDER BY media DESC";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, hackathonId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                double media = rs.getDouble("media");
                int numVoti = rs.getInt("num_voti");

                // Creo l'oggetto Hackathon con solo l'id
                Hackathon h = new Hackathon();
                h.setId(hackathonId);

                // Creo il Team con costruttore vuoto e setter
                Team team = new Team();
                team.setId(id);
                team.setNome(nome);
                team.setHackathon(h);
                team.setProgresso(0);
                
                // Carica i voti effettivi per questo team
                List<Voto> votiTeam = new ArrayList<>();
                String votiSql = "SELECT id_giudice, punteggio FROM voto WHERE id_team = ?";
                try (PreparedStatement votiStmt = conn.prepareStatement(votiSql)) {
                    votiStmt.setInt(1, id);
                    ResultSet votiRs = votiStmt.executeQuery();
                    while (votiRs.next()) {
                        int idGiudice = votiRs.getInt("id_giudice");
                        int punteggio = votiRs.getInt("punteggio");
                        votiTeam.add(new Voto(idGiudice, id, punteggio));
                    }
                }
                team.setVoti(votiTeam);

                // Qui aggiungo il team alla classifica
                classifica.aggiungiTeam(team);

                System.out.println("DEBUG Team: " + nome + " ID: " + id + " Media: " + media + " Num voti: " + numVoti);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classifica;
    }

}
