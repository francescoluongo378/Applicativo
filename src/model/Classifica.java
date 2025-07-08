package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta la graduatoria dei team in base ai voti ricevuti.
 * <p>
 * La classifica contiene una lista di team ordinati per punteggio.
 * La lista è dichiarata come {@code final}, il che significa che il riferimento
 * alla lista non può essere modificato dopo l'inizializzazione nel costruttore.
 * Questo non impedisce di modificare il contenuto della lista (aggiungere/rimuovere team).
 * </p>
 * 
 * @author Sistema Gestione Hackathon
 * @version 1.0
 */
public class Classifica {
    /**
     * Lista dei team ordinati per punteggio.
     * <p>
     * Il modificatore {@code final} garantisce che il riferimento a questa lista
     * non possa essere modificato dopo l'inizializzazione nel costruttore.
     * Questo è un esempio di immutabilità selettiva: il riferimento è immutabile,
     * ma il contenuto della lista può essere modificato.
     * </p>
     */
    private final List<Team> teams;

    /**
     * Costruttore di default.
     * Inizializza una lista vuota di team.
     */
    public Classifica() {
        teams = new ArrayList<>();
    }

    /**
     * Aggiunge un team alla classifica.
     * <p>
     * Nonostante la lista {@code teams} sia dichiarata come {@code final},
     * è possibile modificarne il contenuto aggiungendo elementi.
     * </p>
     * 
     * @param team Team da aggiungere alla classifica
     */
    public void aggiungiTeam(Team team) {
        teams.add(team);
    }

    /**
     * Restituisce la lista dei team nella classifica.
     * 
     * @return Lista di oggetti Team
     */
    public List<Team> getTeams() {
        return teams;
    }

    /**
     * Genera una rappresentazione testuale della classifica.
     * <p>
     * Il formato è:
     * <pre>
     * Classifica:
     * 1. NomeTeam1 – PuntiTotali1
     * 2. NomeTeam2 – PuntiTotali2
     * ...
     * </pre>
     * </p>
     * 
     * @return Stringa che rappresenta la classifica
     */
    @Override
    public String toString() {
        if (teams.isEmpty()) {
            return "Nessun voto registrato.";
        }
        StringBuilder sb = new StringBuilder("Classifica:\n");
        int pos = 1;
        for (Team t : teams) {
            sb.append(pos++)
                    .append(". ")
                    .append(t.getNome())
                    .append(" – ")
                    .append(t.getPuntiTotali())
                    .append("\n");
        }
        return sb.toString();
    }
}
