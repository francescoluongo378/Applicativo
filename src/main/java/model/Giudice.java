package hackathon;
public class Giudice extends Utente {
    private static final int PUNTEGGIO_MINIMO = 0;
    private static final int PUNTEGGIO_MASSIMO = 10;

    public Giudice(String id, String nome, String email) {
        super(id, nome, email);
    }

    public boolean valutaTeam(Team team, int punteggio) {
        if (punteggio < PUNTEGGIO_MINIMO || punteggio > PUNTEGGIO_MASSIMO) {
            return false;
        }

        if (!team.getDocumento().isCompleto()) {
            return false;
        }

        Voto voto = new Voto(this, team, punteggio);
        team.aggiungiVoto(voto);
        return true;
    }
}