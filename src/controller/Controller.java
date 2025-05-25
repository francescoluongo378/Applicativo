package controller;

import Hackathon.*;

public class Controller {
    private Hackathon hackathon;

    public Controller(Hackathon hackathon) {
        this.hackathon = hackathon;
    }

    public boolean creaTeam(String nome) {
        if (nome == null || nome.isBlank()) return false;
        Team team = new Team(nome);
        boolean aggiunto = hackathon.getTeams().add(team);
        if (aggiunto) {
            hackathon.getClassifica().aggiungiTeam(team);
        }
        return aggiunto;
    }

    public boolean aggiungiGiudice(String nome, String email) {
        if (nome == null || nome.isBlank() || email == null || email.isBlank()) return false;
        Giudice giudice = new Giudice("G" + (hackathon.getGiudici().size() + 1), nome, email);
        return hackathon.getGiudici().add(giudice);
    }

    public Team trovaTeam(String nome) {
        if (nome == null) return null;
        return hackathon.getTeams().stream()
                .filter(t -> t.getNome().equalsIgnoreCase(nome))
                .findFirst().orElse(null);
    }
    public Giudice trovaGiudice(String nome) {
        if (nome == null) return null;
        return hackathon.getGiudici().stream()
                .filter(g -> g.getNome().equalsIgnoreCase(nome))
                .findFirst().orElse(null);}

    public boolean aggiornaProgressoTeam(String nomeTeam, int progresso) {
        Team t = trovaTeam(nomeTeam);
        if (t == null) return false;
        t.aggiornaProgresso(progresso);
        return true;
    }

    public boolean valutaTeam(String nomeGiudice, String nomeTeam, int voto) {
        Giudice g = trovaGiudice(nomeGiudice);
        Team t = trovaTeam(nomeTeam);
        if (g == null || t == null) return false;
        return g.valutaTeam(t, voto);
    }
}
