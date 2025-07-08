package model;

/**
 * Rappresenta un giudice dell'Hackathon.
 * <p>
 * Un giudice è un utente che valuta i progetti dei team
 * assegnando voti. I giudici sono assegnati a un Hackathon
 * specifico e possono votare tutti i team partecipanti.
 * </p>

 */
public class Giudice extends Utente {

    /**
     * Costruttore con ID, nome, email e password.
     * <p>
     * Crea un nuovo giudice con il ruolo "giudice".
     * </p>
     * 
     * @param id ID del giudice
     * @param nome Nome del giudice
     * @param email Email del giudice
     * @param password Password del giudice
     */
    public Giudice(Integer id, String nome, String email, String password) {
        super(id, nome, email, password, "giudice");
    }

    /**
     * Costruttore con ID, nome ed email.
     * <p>
     * Questo costruttore è utilizzato quando si carica un giudice
     * dal database ma non è necessaria la password.
     * </p>
     * 
     * @param id ID del giudice
     * @param nome Nome del giudice
     * @param email Email del giudice
     */
    public Giudice(int id, String nome, String email) {
        super(id, nome, email);
    }
}
