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
        String sql = "INSERT INTO partecipante (nome, email) VALUES (?, ?)";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, partecipante.getNome());
            stmt.setString(2, partecipante.getEmail());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        partecipante.setId(generatedKeys.getInt(1)); // assegna id generato
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Partecipante trovaPartecipantePerId(String id) {
        String sql = "SELECT id, nome, email FROM partecipante WHERE id = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
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

    // Metodo nuovo per login: trova partecipante per email
    public Partecipante trovaPerEmail(String email) {
        String sql = "SELECT id, nome, email FROM partecipante WHERE email = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
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
        String sql = "SELECT id, nome, email FROM partecipante";
        try (Connection conn = ConnessioneDatabase.getConnection();
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
        // puoi implementare o richiamare trovaPartecipantePerId
        return trovaPartecipantePerId(String.valueOf(id));
    }

    @Override
    public boolean aggiorna(Partecipante p) {
        String sql = "UPDATE partecipante SET nome = ?, email = ? WHERE id = ?";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getEmail());
            stmt.setInt(3, p.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Partecipante> listaPartecipanti() {
        return findAll();
    }

    @Override
    public void aggiornaPartecipante(Partecipante partecipante) {
        aggiorna(partecipante);
    }


    @Override
    public void salvaPartecipanteNelTeam(String nomePartecipante, int id) {
        // da implementare se serve
    }

    @Override
    public boolean salva(Partecipante p) {
        return true;
    }
}
