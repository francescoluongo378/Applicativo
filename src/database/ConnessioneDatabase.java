package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnessioneDatabase {

    /** URL di connessione al database PostgreSQL */
    private static final String URL = "jdbc:postgresql://localhost:5432/Model";
    
    /** Nome utente per l'accesso al database */
    private static final String USER = "postgres";
    
    /** Password per l'accesso al database */
    private static final String PASSWORD = "kekkobello123";
    
    /** Istanza unica della classe (pattern Singleton) */
    private static ConnessioneDatabase instance;
    
    /** Connessione al database */
    private Connection connection;

    /**
     * Costruttore privato per il pattern Singleton.
     * <p>
     * Inizializza la connessione al database. Il costruttore è privato
     * per impedire la creazione di istanze multiple della classe.
     * </p>
     */
    private ConnessioneDatabase() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Errore nella connessione al database: " + e.getMessage());
        }
    }

    /**
     * Restituisce l'istanza unica della classe (pattern Singleton).
     * <p>
     * Se l'istanza non esiste, viene creata. Altrimenti, viene restituita
     * l'istanza esistente.
     * </p>
     * 
     * @return Istanza unica di ConnessioneDatabase
     */
    public static ConnessioneDatabase getInstance() {
        if (instance == null) {
            instance = new ConnessioneDatabase();
        }
        return instance;
    }
    
    /**
     * Restituisce una connessione al database.
     * <p>
     * Se la connessione è chiusa o null, viene creata una nuova connessione.
     * </p>
     * 
     * @return Connessione al database
     * @throws SQLException Se si verifica un errore durante la connessione
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}

