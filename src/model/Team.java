package model;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private int id;
    private String nome;
    private String descrizione;
    private int progresso;
    private int puntiTotali;
    private Hackathon hackathon;
    private List<Voto> voti;

    public Team() {
        voti = new ArrayList<>();
    }

    public Team(int id, String nome, Hackathon hackathon) {
        this.id = id;
        this.nome = nome;
        this.hackathon = hackathon;
        this.voti = new ArrayList<>();
    }

    // Getter e setter

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public int getProgresso() { return progresso; }
    public void setProgresso(int progresso) { this.progresso = progresso; }

    public Hackathon getHackathon() { return hackathon; }
    public void setHackathon(Hackathon hackathon) { this.hackathon = hackathon; }
    public void setPuntiTotali(int puntiTotali) {
        this.puntiTotali = puntiTotali;
    }
    public List<Voto> getVoti() { return voti; }
    public void setVoti(List<Voto> voti) { this.voti = voti; }

    public void aggiungiVoto(Voto voto) {
        voti.add(voto);
    }

    public int getPuntiTotali() {
        if (voti.isEmpty()) {
            return 0;
        }
        
        int somma = 0;
        for (Voto v : voti) {
            somma += v.getPunteggio();
        }
        return somma;
    }
    
    public double getPunteggioMedio() {
        if (voti.isEmpty()) {
            return 0.0;
        }
        
        int somma = 0;
        for (Voto v : voti) {
            somma += v.getPunteggio();
        }
        return (double) somma / voti.size();
    }
}
