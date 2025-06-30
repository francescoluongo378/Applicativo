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

}
