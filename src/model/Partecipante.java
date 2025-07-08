package model;

/**
 * Rappresenta un partecipante all'Hackathon.
 * <p>
 * Un partecipante è un utente che può iscriversi a un team.
 * Ogni team può avere al massimo 3 partecipanti, ma questo limite
 * è implementato nel Controller e nella GUI, non in questa classe.
 * </p>
 */
public class Partecipante extends Utente {
    /** ID del team a cui appartiene il partecipante */
    private int teamId;

    /**
     * Costruttore con ID, nome, email e password.
     * <p>
     * Crea un nuovo partecipante con il ruolo "partecipante".
     * </p>
     * 
     * @param id ID del partecipante
     * @param nome Nome del partecipante
     * @param email Email del partecipante
     * @param password Password del partecipante
     */
    public Partecipante(Integer id, String nome, String email, String password) {
        super(id, nome, email, password, "partecipante");
    }

    /**
     * Costruttore con ID, nome ed email.
     * <p>
     * Questo costruttore è utilizzato quando si carica un partecipante
     * dal database ma non è necessaria la password.
     * </p>
     * 
     * @param id ID del partecipante
     * @param nome Nome del partecipante
     * @param email Email del partecipante
     */
    public Partecipante(int id, String nome, String email) {
        super(id, nome, email);
    }

    /**
     * Restituisce l'ID del team a cui appartiene il partecipante.
     * 
     * @return ID del team, 0 se non appartiene a nessun team
     */
    public int getTeamId() {
        return teamId;
    }

    /**
     * Imposta l'ID del team a cui appartiene il partecipante.
     * <p>
     * Questo metodo è utilizzato quando un partecipante si unisce a un team
     * o cambia team.
     * </p>
     * 
     * @param teamId ID del team
     */
    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}

