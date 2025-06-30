package postgresdao;

import dao.UtenteDAO;
import database.ConnessioneDatabase;
import model.Utente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresUtenteDAO implements UtenteDAO {

    @Override
    public boolean salva(Utente u) {
        String sql = "INSERT INTO utente (nome,email,password,ruolo) VALUES (?,?,?,?)";
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, u.getNome());
            p.setString(2, u.getEmail());
            p.setString(3, u.getPassword());
            p.setString(4, u.getRuolo());
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Utente trovaPerEmail(String email) {
        String sql = "SELECT id, nome, email, password, ruolo FROM utente WHERE email=?";
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, email);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    return new Utente(
                            r.getInt("id"),
                            r.getString("nome"),
                            r.getString("email"),
                            r.getString("password"),
                            r.getString("ruolo")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Utente trovaPerId(int id) {
        String sql = "SELECT id, nome, email, password, ruolo FROM utente WHERE id=?";
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    return new Utente(
                            r.getInt("id"),
                            r.getString("nome"),
                            r.getString("email"),
                            r.getString("password"),
                            r.getString("ruolo")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Utente> findAll() {
        List<Utente> l = new ArrayList<>();
        String sql = "SELECT id, nome, email, password, ruolo FROM utente";
        try (Connection c = ConnessioneDatabase.getConnection();
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery(sql)) {
            while (r.next()) {
                l.add(new Utente(
                        r.getInt("id"),
                        r.getString("nome"),
                        r.getString("email"),
                        r.getString("password"),
                        r.getString("ruolo")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    @Override
    public boolean aggiorna(Utente utente) {
        String sql = "UPDATE utente SET nome=?, email=?, password=?, ruolo=? WHERE id=?";
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, utente.getNome());
            p.setString(2, utente.getEmail());
            p.setString(3, utente.getPassword());
            p.setString(4, utente.getRuolo());
            p.setInt(5, utente.getId());
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean elimina(int id) {
        String sql = "DELETE FROM utente WHERE id=?";
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

