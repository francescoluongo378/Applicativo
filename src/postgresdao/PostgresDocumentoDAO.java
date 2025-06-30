package postgresdao;

import dao.DocumentoDAO;
import database.ConnessioneDatabase;
import model.Documento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresDocumentoDAO implements DocumentoDAO {

    private Connection conn;

    public PostgresDocumentoDAO() {
        try {
            this.conn = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean salvaDocumento(Documento documento) {
        String sql = "INSERT INTO documento (titolo, id_partecipante) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, documento.getTitolo());
            stmt.setInt(2, documento.getIdPartecipante());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Errore durante il salvataggio del documento.");
        }
        return false;
    }

    @Override
    public List<Documento> getDocumentiPerPartecipante(int idPartecipante) {
        List<Documento> documenti = new ArrayList<>();
        String sql = "SELECT * FROM documento WHERE id_partecipante = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPartecipante);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Documento documento = new Documento();
                documento.setId(rs.getInt("id"));
                documento.setTitolo(rs.getString("titolo"));
                documento.setIdPartecipante(rs.getInt("id_partecipante"));
                documenti.add(documento);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Errore durante il recupero dei documenti.");
        }

        return documenti;
    }

    @Override
    public Documento findByPartecipante(int idPartecipante) {
        return null;
    }
}

