package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Team {
    private int id;
    private String nome;
    private String descrizione;
    private int progresso;
    private int hackathonId;
    private int puntiTotali;  // usato per la classifica

    public Team() {}  // costruttore vuoto necessario

    // Costruttore completo
    public Team(int id, String nome, String descrizione, int progresso, int hackathonId) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.progresso = progresso;
        this.hackathonId = hackathonId;
    }

    // Costruttore usato nel DAO Classifica (id, nome, hackathonId)
    public Team(int id, String nome, int hackathonId) {
        this.id = id;
        this.nome = nome;
        this.hackathonId = hackathonId;
    }

    // Costruttore per il caso con solo nome (es. PostgresVotoDAO)
    public Team(String nome) {
        this.nome = nome;
    }

    // getter e setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getProgresso() {
        return progresso;
    }

    public void setProgresso(int progresso) {
        this.progresso = progresso;
    }

    public int getHackathonId() {
        return hackathonId;
    }

    public void setHackathonId(int hackathonId) {
        this.hackathonId = hackathonId;
    }

    public int getPuntiTotali() {
        return puntiTotali;
    }

    public void setPuntiTotali(int puntiTotali) {
        this.puntiTotali = puntiTotali;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", progresso=" + progresso +
                ", hackathonId=" + hackathonId +
                ", puntiTotali=" + puntiTotali +
                '}';
    }


    private List<Voto> voti = new ArrayList<>();

    public void aggiungiVoto(Voto nuovoVoto) {
        voti.add(nuovoVoto);
        this.puntiTotali += nuovoVoto.getPunteggio();
    }

    public List<Voto> getVoti() {
        return voti;
    }

}
