package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {

    private static final String URL = "jdbc:postgresql://localhost:5432/Model";
    private static final String USER = "postgres";
    private static final String PASSWORD = "kekkobello123";
    
    private static ConnessioneDatabase instance;
    private Connection connection;

    // Costruttore privato per il pattern Singleton
    private ConnessioneDatabase() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Errore nella connessione al database: " + e.getMessage());
        }
    }

    // Metodo per ottenere l'istanza singleton
    public static ConnessioneDatabase getInstance() {
        if (instance == null) {
            instance = new ConnessioneDatabase();
        }
        return instance;
    }
    
    // Metodo per ottenere la connessione dall'istanza
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}

