package postgresdao;

import dao.VotoDAO;
import database.ConnessioneDatabase;
import model.Voto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresVotoDAO implements VotoDAO {

    @Override
    public boolean salvaVoto(Voto voto) {
        return salvaVoto(voto.getIdGiudice(), voto.getIdTeam(), voto.getPunteggio());
    }

    @Override
    public boolean salvaVoto(int idGiudice, int idTeam, int punteggio) {
        String sql = "INSERT INTO voto (id_giudice, id_team, punteggio) VALUES (?, ?, ?)";
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idGiudice);
            stmt.setInt(2, idTeam);
            stmt.setInt(3, punteggio);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Voto> listaVoti() {
        List<Voto> lista = new ArrayList<>();
        String sql = "SELECT id_giudice, id_team, punteggio FROM voto";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idGiudice = rs.getInt("id_giudice");
                int idTeam = rs.getInt("id_team");
                int punteggio = rs.getInt("punteggio");

                Voto voto = new Voto(idGiudice, idTeam, punteggio);
                lista.add(voto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<Voto> listaVotiPerTeam(int idTeam) {
        List<Voto> lista = new ArrayList<>();
        String sql = "SELECT id_giudice, id_team, punteggio FROM voto WHERE id_team = ?";

        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTeam);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idGiudice = rs.getInt("id_giudice");
                    int punteggio = rs.getInt("punteggio");

                    Voto voto = new Voto(idGiudice, idTeam, punteggio);
                    lista.add(voto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
