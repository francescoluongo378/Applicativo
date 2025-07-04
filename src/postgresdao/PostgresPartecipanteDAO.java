package postgresdao;
import database.*;
import dao.*;
import model.Partecipante;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresPartecipanteDAO implements PartecipanteDAO {

    @Override
    public boolean salvaPartecipante(Partecipante partecipante) {

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
                
                int rows = stmt.executeUpdate();
                return rows > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Errore nel salvataggio del partecipante: " + e.getMessage());
            }
        } else {
            // If the participant doesn't have an ID, this is an error
            System.err.println("Errore: tentativo di salvare un partecipante senza ID");
        }
        return false;
    }

    @Override
    public Partecipante trovaPartecipantePerId(String id) {
        String sql = "SELECT p.id, u.nome, u.email FROM partecipante p JOIN utente u ON p.id = u.id WHERE p.id = ?";
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Partecipante(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("email")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    public Partecipante findById(int id) {

        return trovaPartecipantePerId(String.valueOf(id));
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
    public List<Partecipante> findAllByHackathonId(int hackathonId) {
        List<Partecipante> partecipanti = new ArrayList<>();
        String sql = "SELECT p.id, u.nome, u.email, p.team_id FROM partecipante p JOIN utente u ON p.id = u.id";
        
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Partecipante p = new Partecipante(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email")
                );
                

                int teamId = rs.getInt("team_id");
                if (!rs.wasNull()) {
                    p.setTeamId(teamId);
                }
                
                partecipanti.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Errore nel recupero dei partecipanti: " + e.getMessage());
        }
        
        return partecipanti;
    }

    @Override
    public void salvaPartecipanteNelTeam(int idPartecipante, int idTeam) {
        try {

            Partecipante p = findById(idPartecipante);
            if (p == null) {

                try (Connection conn = ConnessioneDatabase.getInstance().getConnection()) {
                    String sql = "SELECT id, nome, email, password, ruolo FROM utente WHERE id = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setInt(1, idPartecipante);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {

                                String nome = rs.getString("nome");
                                String email = rs.getString("email");
                                new Partecipante(idPartecipante, nome, email);


                                String insertSql = "INSERT INTO partecipante (id, team_id) VALUES (?, ?)";
                                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                                    insertStmt.setInt(1, idPartecipante);
                                    insertStmt.setInt(2, idTeam);
                                    insertStmt.executeUpdate();
                                    System.out.println("Created new participant record for ID: " + idPartecipante + " in team: " + idTeam);
                                    return;
                                }
                            } else {
                                System.err.println("User not found with ID: " + idPartecipante);
                                return;
                            }
                        }
                    }
                }
            }
            try (Connection conn = ConnessioneDatabase.getInstance().getConnection()) {
                String checkSql = "SELECT team_id FROM partecipante WHERE id = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setInt(1, idPartecipante);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next()) {
                            Integer currentTeamId = rs.getObject("team_id", Integer.class);
                            if (currentTeamId != null) {

                                String updateSql = "UPDATE partecipante SET team_id = ? WHERE id = ?";
                                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                                    updateStmt.setInt(1, idTeam);
                                    updateStmt.setInt(2, idPartecipante);
                                    updateStmt.executeUpdate();
                                    System.out.println("Updated participant ID: " + idPartecipante + " to team: " + idTeam);
                                    p.setTeamId(idTeam);
                                    return;
                                }
                            }
                        }
                    }
                }
            }


            p.setTeamId(idTeam);
            aggiorna(p);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Errore nel salvare il partecipante nel team: " + e.getMessage());
        }
    }
    
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
