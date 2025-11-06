package com.example.demo.team.entity;

import com.example.demo.format.strategy.Format;
import com.example.demo.profile.entity.Profile;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private List<Profile> players;
    private Format format;

    public Team(Format format) {
        this.players = new ArrayList<>();
        this.format = format;
    }

    public boolean addPlayer(Profile player) {
        if (players.size() < format.getMaxPlayersPerTeam()) {
            players.add(player);
            return true;
        }
        return false;
    }

    public int getCurrentSize() {
        return players.size();
    }

    public int getMaxSize() {
        return format.getMaxPlayersPerTeam();
    }

    public boolean isFull(){
        return getCurrentSize() >= getMaxSize();
    }

}