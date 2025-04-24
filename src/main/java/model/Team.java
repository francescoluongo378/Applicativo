package hackathon;
import java.util.ArrayList;
import java.util.List;

public class Team {
    private String nome;
    private List<Partecipante> membri;
    private List<Voto> voti;
    private Documento documento;
    private static final int MEMBRI_MAX = 3;

    public Team(String nome) {
        this.nome = nome;
        this.membri = new ArrayList<>();
        this.voti = new ArrayList<>();
        this.documento = new Documento();
    }

    public boolean aggiungiMembro(Partecipante p) {
        if (membri.size() < MEMBRI_MAX) {
            membri.add(p);
            return true;
        }
        return false;
    }

    public void aggiungiVoto(Voto voto) {
        voti.add(voto);
    }

    public int getPunteggio() {
        int totale = 0;
        for (Voto voto : voti) {
            totale += voto.getPunteggio();
        }
        return totale;
    }

    public void aggiornaProgresso(int nuovoProgresso) {
        documento.setProgresso(nuovoProgresso);
    }

    public String getNome() { return nome; }
    public List<Partecipante> getMembri() { return new ArrayList<>(membri); }
    public int getProgresso() { return documento.getProgresso(); }
    public Documento getDocumento() { return documento; }
}




    

