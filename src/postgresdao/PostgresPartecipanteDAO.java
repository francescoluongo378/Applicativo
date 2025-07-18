package postgresdao;
import database.*;
import dao.*;
import model.Partecipante;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresPartecipanteDAO implements PartecipanteDAO {

    @Override
    public void salvaPartecipante(Partecipante partecipante) {

        if (partecipante.getId() != null && partecipante.getId() > 0) {
            String sql = "INSERT INTO partecipante (id, team_id) VALUES (?, ?)";
            try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, partecipante.getId());
                

                if (partecipante.getTeamId() > 0) {
                    stmt.setInt(2, partecipante.getTeamId());
                } else {
                    stmt.setNull(2, java.sql.Types.INTEGER);
                }

                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Errore nel salvataggio del partecipante: " + e.getMessage());
            }
        } else {
            // If the participant doesn't have an ID, this is an error
            System.err.println("Errore: tentativo di salvare un partecipante senza ID");
        }
    }

    @Override
    public Partecipante findById(int id) {
        return null;
    }

    @Override
    public List<Partecipante> findAll() {
        List<Partecipante> lista = new ArrayList<>();
        String sql = "SELECT p.id, u.nome, u.email FROM partecipante p JOIN utente u ON p.id = u.id";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Partecipante p = new Partecipante(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email")
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }



    @Override
    public boolean aggiorna(Partecipante p) {
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection()) {

            String sql = "UPDATE partecipante SET nome = ?, email = ?, team_id = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, p.getNome());
                stmt.setString(2, p.getEmail());
                

                if (p.getTeamId() > 0) {
                    stmt.setInt(3, p.getTeamId());
                } else {
                    stmt.setNull(3, java.sql.Types.INTEGER);
                }
                
                stmt.setInt(4, p.getId());
                
                int rows = stmt.executeUpdate();
                return rows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Errore nell'aggiornamento del partecipante: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void salvaPartecipanteNelTeam(int idPartecipante, int idTeam) {

    }


    /**
     * Conta il numero di partecipanti in un team.
     * <p>
     * Questo metodo è utilizzato per verificare se un team ha raggiunto
     * il limite massimo di 3 partecipanti. Il limite è implementato con
     * controlli espliciti nel Controller e nella GUI che utilizzano
     * questo metodo per ottenere il conteggio attuale.
     * </p>
     * 
     * @param idTeam ID del team di cui contare i partecipanti
     * @return Numero di partecipanti nel team, 0 in caso di errore
     */
    public int contaPartecipantiInTeam(int idTeam) {
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection()) {

            String sql = "SELECT COUNT(*) AS num_partecipanti FROM partecipante WHERE team_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idTeam);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    return rs.getInt("num_partecipanti");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Errore nel contare i partecipanti nel team: " + e.getMessage());
        }
        return 0;
    }


    @Override
    public boolean salva(Partecipante p) {

        Partecipante existing = findById(p.getId());
        if (existing != null) {

            return aggiorna(p);
        } else {

            String sql = "INSERT INTO partecipante (id, nome, email, team_id) VALUES (?, ?, ?, ?)";
            try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, p.getId());
                stmt.setString(2, p.getNome());
                stmt.setString(3, p.getEmail());
                
                // Set team_id if available
                if (p.getTeamId() > 0) {
                    stmt.setInt(4, p.getTeamId());
                } else {
                    stmt.setNull(4, java.sql.Types.INTEGER);
                }
                
                int rows = stmt.executeUpdate();
                return rows > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Errore nel salvataggio del partecipante: " + e.getMessage());
            }
            return false;
        }
    }
    
    @Override
    public boolean elimina(int id) {
        String sql = "DELETE FROM partecipante WHERE id = ?";
        
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Errore durante l'eliminazione del partecipante: " + e.getMessage());
            return false;
        }
    }
}
