package com.example.demo.Lobby.entity;

import com.example.demo.format.strategy.Format;
import com.example.demo.team.entity.Team;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
    private static final int MAX_TEAMS = 2;
    private List<Team> teams;
    private Format format;

    public Lobby(Format format) {
        this.teams = new ArrayList<>();
        this.format = format;
        Team team1 = new Team(format);
        Team team2 = new Team(format);

        teams.add(team1);
        teams.add(team2);
    }

    public boolean isLobbyValid() {
        if (teams.size() != MAX_TEAMS) {
            return false;
        }

        for (Team team : teams) {
            if (team.getCurrentSize() != team.getMaxSize()) {
                return false;
            }
        }

        return true;
    }
}