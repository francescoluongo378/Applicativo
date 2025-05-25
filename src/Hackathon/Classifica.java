package Hackathon;

import java.util.ArrayList;
import java.util.List;

public class Classifica {
    private List<Team> Teams;

    public Classifica() {
        this.Teams = new ArrayList<>();
    }

    public void aggiungiTeam(Team team) {
        Teams.add(team);
    }

    public List<Team> getTeams() {
        return Teams;
    }
}

