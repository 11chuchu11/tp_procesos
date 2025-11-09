package com.example.demo.scrim.entity;

import com.example.demo.enums.ScrimStatus;
import com.example.demo.format.factory.FormatFactory;
import com.example.demo.format.strategy.Format;
import com.example.demo.game.entity.Game;
import com.example.demo.Lobby.entity.Lobby;
import com.example.demo.profile.entity.Profile;
import com.example.demo.scrim.factory.ScrimStateFactory;
import com.example.demo.scrim.state.ScrimState;
import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private com.example.demo.user.entity.User createdBy;

    @Transient
    private Format format;

    @Column(name = "format_type")
    private String formatType;

    @Column(name = "min_tier")
    private String minTier;

    @Column(name = "max_tier")
    private String maxTier;

    @Column(name = "region")
    private String region;

    @Column(name = "scheduled_time", nullable = false)
    private LocalDateTime scheduledTime;

    public Scrim() {
        this.status = ScrimStatus.SEARCHING;
        this.state = ScrimStateFactory.fromStatus(this.status);
    }

    public Scrim(String formatType, Game game, com.example.demo.user.entity.User createdBy) {
        this.status = ScrimStatus.SEARCHING;
        this.state = ScrimStateFactory.fromStatus(this.status);
        this.formatType = formatType;
        this.format = FormatFactory.fromString(formatType);
        this.lobby = new Lobby(formatType);
        this.game = game;
        this.createdBy = createdBy;
    }

    public Scrim(String formatType, Game game, com.example.demo.user.entity.User createdBy, String minTier,
            String maxTier, String region, LocalDateTime scheduledTime) {
        this.status = ScrimStatus.SEARCHING;
        this.state = ScrimStateFactory.fromStatus(this.status);
        this.formatType = formatType;
        this.format = FormatFactory.fromString(formatType);
        this.lobby = new Lobby(formatType);
        this.game = game;
        this.createdBy = createdBy;
        this.minTier = minTier;
        this.maxTier = maxTier;
        this.region = region;
        this.scheduledTime = scheduledTime;
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
        boolean added = lobby.addProfileRandomly(profile);
        
        if (added && isLobbyFull() && status == ScrimStatus.SEARCHING) {
            setState(ScrimStateFactory.fromStatus(ScrimStatus.LOBBYREADY));
        }
        
        return added;
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

    public com.example.demo.user.entity.User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(com.example.demo.user.entity.User createdBy) {
        this.createdBy = createdBy;
    }

    public String getMinTier() {
        return minTier;
    }

    public void setMinTier(String minTier) {
        this.minTier = minTier;
    }

    public String getMaxTier() {
        return maxTier;
    }

    public void setMaxTier(String maxTier) {
        this.maxTier = maxTier;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public boolean isScheduledTimeReached() {
        return scheduledTime != null && LocalDateTime.now().isAfter(scheduledTime);
    }

    public boolean isPlayerEligible(Profile profile) {
        if (minTier == null && maxTier == null) {
            return true;
        }

        String playerTier = profile.getTier();
        if (playerTier == null) {
            return false;
        }

        if (minTier != null && maxTier == null) {
            return compareTiers(playerTier, minTier) >= 0;
        }

        if (minTier == null && maxTier != null) {
            return compareTiers(playerTier, maxTier) <= 0;
        }

        return compareTiers(playerTier, minTier) >= 0 && compareTiers(playerTier, maxTier) <= 0;
    }

    private int compareTiers(String tier1, String tier2) {
        return tier1.compareToIgnoreCase(tier2);
    }
}
