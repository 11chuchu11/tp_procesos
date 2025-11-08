package com.example.demo.scrim.entity;

import com.example.demo.enums.ScrimStatus;
import com.example.demo.format.factory.FormatFactory;
import com.example.demo.format.strategy.Format;
import com.example.demo.game.entity.Game;
import com.example.demo.lobby.entity.Lobby;
import com.example.demo.profile.entity.Profile;
import com.example.demo.scrim.factory.ScrimStateFactory;
import com.example.demo.scrim.state.ScrimState;
import jakarta.persistence.*;

@Entity
@Table(name = "scrims")
public class Scrim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrim_id")
    private Long scrimId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ScrimStatus status;

    @Transient
    private ScrimState state;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lobby_id")
    private Lobby lobby;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Transient
    private Format format;

    @Column(name = "format_type")
    private String formatType;

    public Scrim() {
        this.status = ScrimStatus.SEARCHING;
        this.state = ScrimStateFactory.fromStatus(this.status);
    }

    public Scrim(String formatType, Game game) {
        this.status = ScrimStatus.SEARCHING;
        this.state = ScrimStateFactory.fromStatus(this.status);
        this.formatType = formatType;
        this.format = FormatFactory.fromString(formatType);
        this.lobby = new Lobby(formatType);
        this.game = game;
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    public void initState() {
        if (this.status != null) {
            this.state = ScrimStateFactory.fromStatus(this.status);
        }
        if (this.formatType != null) {
            this.format = FormatFactory.fromString(this.formatType);
        }
    }

    public void setState(ScrimState state) {
        this.state = state;
        this.status = ScrimStatus.valueOf(state.getStateName());
    }

    public void apply() {
        this.state.apply(this);
    }

    public void cancel() {
        this.state.cancel(this);
    }

    public void finish() {
        this.state.finish(this);
    }

    public boolean addPlayerToLobby(Profile profile) {
        if (lobby == null) {
            return false;
        }
        return lobby.addProfileRandomly(profile);
    }

    public boolean isLobbyFull() {
        if (lobby == null) {
            return false;
        }
        return lobby.isLobbyFull();
    }

    public boolean isProfileInLobby(Profile profile) {
        if (lobby == null) {
            return false;
        }
        return lobby.isProfileInLobby(profile);
    }

    // Getters and setters
    public Long getScrimId() {
        return scrimId;
    }

    public void setScrimId(Long scrimId) {
        this.scrimId = scrimId;
    }

    public ScrimStatus getStatus() {
        return status;
    }

    public void setStatus(ScrimStatus status) {
        this.status = status;
    }

    public ScrimState getState() {
        return state;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
        if (format != null) {
            this.formatType = FormatFactory.toString(format);
        }
    }

    public String getFormatType() {
        return formatType;
    }

    public void setFormatType(String formatType) {
        this.formatType = formatType;
        this.format = FormatFactory.fromString(formatType);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
