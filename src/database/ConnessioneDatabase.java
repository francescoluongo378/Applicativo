package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {

    private static final String URL = "jdbc:postgresql://localhost:5432/Model";
    private static final String USER = "postgres";
    private static final String PASSWORD = "kekkobello123";

    // Metodo statico per ottenere la connessione al DB
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static ConnessioneDatabase getInstance() {
        return null;
    }
}

