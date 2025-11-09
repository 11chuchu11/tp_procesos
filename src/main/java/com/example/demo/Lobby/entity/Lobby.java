package com.example.demo.Lobby.entity;

import com.example.demo.format.factory.FormatFactory;
import com.example.demo.format.strategy.Format;
import com.example.demo.profile.entity.Profile;
import com.example.demo.scrim.entity.Scrim;
import com.example.demo.team.entity.Team;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "lobbies")
public class Lobby {

    private static final int MAX_TEAMS = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lobby_id")
    private Long lobbyId;

    @OneToOne(mappedBy = "lobby")
    private Scrim scrim;

    @OneToMany(mappedBy = "lobby", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Team> teams;

    @Column(name = "format_type", nullable = false)
    private String formatType;

    @Transient
    private Format format;

    @Transient
    private Random random = new Random();

    public Lobby() {
        this.teams = new ArrayList<>();
    }

    public Lobby(String formatType) {
        this.teams = new ArrayList<>();
        this.formatType = formatType;
        this.format = FormatFactory.fromString(formatType);
        initializeTeams();
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    public void initFormat() {
        if (this.formatType != null) {
            this.format = FormatFactory.fromString(this.formatType);
        }
        if (this.random == null) {
            this.random = new Random();
        }
    }

    private void initializeTeams() {
        Team team1 = new Team(formatType);
        Team team2 = new Team(formatType);
        
        team1.setLobby(this);
        team2.setLobby(this);
        
        teams.add(team1);
        teams.add(team2);
    }

    public boolean addProfileRandomly(Profile profile) {
        if (isLobbyFull()) {
            return false;
        }

        // Try to balance teams
        Team team1 = teams.get(0);
        Team team2 = teams.get(1);

        // If one team is full, add to the other
        if (team1.isFull() && !team2.isFull()) {
            return team2.addPlayer(profile);
        } else if (team2.isFull() && !team1.isFull()) {
            return team1.addPlayer(profile);
        }

        // If both have space, randomly select
        int teamIndex = random.nextInt(2);
        Team selectedTeam = teams.get(teamIndex);
        
        if (selectedTeam.addPlayer(profile)) {
            return true;
        }

        // If selected team is full, try the other one
        Team otherTeam = teams.get(1 - teamIndex);
        return otherTeam.addPlayer(profile);
    }

    public boolean isLobbyFull() {
        if (teams.size() != MAX_TEAMS) {
            return false;
        }

        for (Team team : teams) {
            if (!team.isFull()) {
                return false;
            }
        }

        return true;
    }

    public boolean isProfileInLobby(Profile profile) {
        if (profile == null || profile.getProfileId() == null) {
            return false;
        }

        for (Team team : teams) {
            for (Profile p : team.getPlayers()) {
                if (p.getProfileId() != null && p.getProfileId().equals(profile.getProfileId())) {
                    return true;
                }
            }
        }

        return false;
    }

    // Getters and setters
    public Long getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public Scrim getScrim() {
        return scrim;
    }

    public void setScrim(Scrim scrim) {
        this.scrim = scrim;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public String getFormatType() {
        return formatType;
    }

    public void setFormatType(String formatType) {
        this.formatType = formatType;
        this.format = FormatFactory.fromString(formatType);
    }

    public Format getFormat() {
        return format;
    }
}