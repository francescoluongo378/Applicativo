package hackathon;

import java.util.ArrayList;
import java.util.List;

public class Classifica {
    private List<Team> teams;

    public Classifica() {
        this.teams = new ArrayList<>();
    }

    public void aggiungiTeam(Team team) {
        teams.add(team);
    }

    public List<Team> getTeams() {
        return teams;
    }
}

