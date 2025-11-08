package com.example.demo.team.entity;

import com.example.demo.format.factory.FormatFactory;
import com.example.demo.format.strategy.Format;
import com.example.demo.lobby.entity.Lobby;
import com.example.demo.profile.entity.Profile;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long teamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lobby_id")
    private Lobby lobby;

    @ManyToMany
    @JoinTable(
        name = "team_profiles",
        joinColumns = @JoinColumn(name = "team_id"),
        inverseJoinColumns = @JoinColumn(name = "profile_id")
    )
    private List<Profile> players;

    @Column(name = "format_type", nullable = false)
    private String formatType;

    @Transient
    private Format format;

    public Team() {
        this.players = new ArrayList<>();
    }

    public Team(String formatType) {
        this.players = new ArrayList<>();
        this.formatType = formatType;
        this.format = FormatFactory.fromString(formatType);
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    public void initFormat() {
        if (this.formatType != null) {
            this.format = FormatFactory.fromString(this.formatType);
        }
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

    public boolean isFull() {
        return getCurrentSize() >= getMaxSize();
    }

    // Getters and setters
    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public List<Profile> getPlayers() {
        return players;
    }

    public void setPlayers(List<Profile> players) {
        this.players = players;
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