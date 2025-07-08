package model;

/**
 * Rappresenta un utente del sistema.
 * <p>
 * Questa è la classe base per tutti gli utenti del sistema, inclusi
 * partecipanti, giudici e organizzatori. Gli attributi nome ed email
 * sono dichiarati come {@code final} per garantire che non vengano
 * modificati dopo la creazione dell'utente.
 * </p>
 *
 */
public class Utente {
    /** Identificativo univoco dell'utente */
    private Integer id;
    
    /** Nome completo dell'utente (immutabile) */
    private final String nome;
    
    /** Indirizzo email dell'utente (immutabile) */
    private final String email;
    
    /** Password per l'accesso */
    private String password;
    
    /** Ruolo dell'utente (partecipante, giudice, organizzatore) */
    private String ruolo;

    /**
     * Costruttore per la registrazione di un nuovo utente.
     * <p>
     * Questo costruttore è utilizzato quando si registra un nuovo utente
     * nel sistema. L'ID non è specificato e verrà assegnato dal database.
     * </p>
     * 
     * @param nome Nome completo dell'utente
     * @param email Indirizzo email dell'utente
     * @param password Password per l'accesso
     * @param ruolo Ruolo dell'utente (partecipante, giudice, organizzatore)
     */
    public Utente(String nome, String email, String password, String ruolo) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
    }

    /**
     * Costruttore per la lettura di un utente dal database.
     * <p>
     * Questo costruttore è utilizzato quando si carica un utente esistente
     * dal database, incluso il suo ID.
     * </p>
     * 
     * @param id Identificativo dell'utente
     * @param nome Nome completo dell'utente
     * @param email Indirizzo email dell'utente
     * @param password Password per l'accesso
     * @param ruolo Ruolo dell'utente (partecipante, giudice, organizzatore)
     */
    public Utente(Integer id, String nome, String email, String password, String ruolo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
    }

    /**
     * Costruttore con ID, nome, email e ruolo.
     * <p>
     * Questo costruttore è utilizzato quando si carica un utente dal database
     * ma non è necessaria la password (ad esempio, per visualizzazioni).
     * </p>
     * 
     * @param id Identificativo dell'utente
     * @param nome Nome completo dell'utente
     * @param email Indirizzo email dell'utente
     * @param ruolo Ruolo dell'utente
     */
    public Utente(int id, String nome, String email, String ruolo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.ruolo = ruolo;
    }

    /**
     * Costruttore con nome, email e ruolo.
     * <p>
     * Questo costruttore è utilizzato quando si crea un nuovo utente
     * senza specificare l'ID e la password.
     * </p>
     * 
     * @param nome Nome completo dell'utente
     * @param email Indirizzo email dell'utente
     * @param ruolo Ruolo dell'utente
     */
    public Utente(String nome, String email, String ruolo) {
        this.nome = nome;
        this.email = email;
        this.ruolo = ruolo;
    }

    /**
     * Costruttore con ID, nome ed email.
     * <p>
     * Questo costruttore è utilizzato quando si carica un utente dal database
     * ma non sono necessari né la password né il ruolo.
     * </p>
     * 
     * @param id Identificativo dell'utente
     * @param nome Nome completo dell'utente
     * @param email Indirizzo email dell'utente
     */
    public Utente(int id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    /**
     * Restituisce l'ID dell'utente.
     * 
     * @return ID dell'utente
     */
    public Integer getId() {
        return id;
    }
    
    /**
     * Restituisce il nome dell'utente.
     * 
     * @return Nome dell'utente
     */
    public String getNome() {
        return nome;
    }
    
    /**
     * Restituisce l'email dell'utente.
     * 
     * @return Email dell'utente
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Restituisce la password dell'utente.
     * 
     * @return Password dell'utente
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Restituisce il ruolo dell'utente.
     * 
     * @return Ruolo dell'utente (partecipante, giudice, organizzatore)
     */
    public String getRuolo() {
        return ruolo;
    }
    
    /**
     * Imposta l'ID dell'utente.
     * <p>
     * Questo metodo è tipicamente utilizzato dal DAO dopo
     * il salvataggio dell'utente nel database.
     * </p>
     * 
     * @param id Nuovo ID dell'utente
     */
    public void setId(Integer id) {
        this.id = id;
    }

}

