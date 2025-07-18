package postgresdao;

import dao.OrganizzatoreDAO;
import model.Organizzatore;
import database.ConnessioneDatabase;

import java.sql.*;

public class PostgresOrganizzatoreDAO implements OrganizzatoreDAO {
    private final ConnessioneDatabase connessione;

    public PostgresOrganizzatoreDAO(ConnessioneDatabase connessione) {
        this.connessione = connessione;
    }

    @Override
    public void salva(Organizzatore organizzatore) throws Exception {
        Connection con = connessione.getConnection();
        String sql = "INSERT INTO organizzatori (id, nome, email, password, ruolo) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, organizzatore.getId());
        ps.setString(2, organizzatore.getNome());
        ps.setString(3, organizzatore.getEmail());
        ps.setString(4, organizzatore.getPassword());
        ps.setString(5, "organizzatore");
        ps.executeUpdate();
        ps.close();
    }

}
