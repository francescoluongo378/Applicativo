package model;

import java.util.ArrayList;
import java.util.List;

public class Classifica {
    private final List<Team> teams;

    public Classifica() {
        teams = new ArrayList<>();
    }

    public void aggiungiTeam(Team team) {
        teams.add(team);
    }

    public List<Team> getTeams() {
        return teams;
    }

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
