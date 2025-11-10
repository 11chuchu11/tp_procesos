package com.example.demo.scrim.builder;

import com.example.demo.Lobby.entity.Lobby;
import com.example.demo.enums.ScrimStatus;
import com.example.demo.game.entity.Game;
import com.example.demo.region.entity.Region;
import com.example.demo.scrim.entity.Scrim;
import com.example.demo.scrim.factory.ScrimStateFactory;
import com.example.demo.tier.entity.Tier;
import com.example.demo.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ScrimBuilder {
    private String formatType;
    private Game game;
    private User createdBy;
    private Tier minTier;
    private Tier maxTier;
    private Region region;
    private LocalDateTime scheduledTime;
    private Lobby lobby;

    public ScrimBuilder withFormatType(String formatType) {
        this.formatType = formatType;
        return this;
    }

    public ScrimBuilder withGame(Game game) {
        this.game = game;
        return this;
    }

    public ScrimBuilder createdBy(User createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ScrimBuilder withMinTier(Tier minTier) {
        this.minTier = minTier;
        return this;
    }

    public ScrimBuilder withMaxTier(Tier maxTier) {
        this.maxTier = maxTier;
        return this;
    }

    public ScrimBuilder withTierRange(Tier minTier, Tier maxTier) {
        this.minTier = minTier;
        this.maxTier = maxTier;
        return this;
    }

    public ScrimBuilder withRegion(Region region) {
        this.region = region;
        return this;
    }

    public ScrimBuilder scheduledFor(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
        return this;
    }

    public ScrimBuilder scheduledInMinutes(int minutes) {
        this.scheduledTime = LocalDateTime.now().plusMinutes(minutes);
        return this;
    }

    public ScrimBuilder scheduledInHours(int hours) {
        this.scheduledTime = LocalDateTime.now().plusHours(hours);
        return this;
    }

    public ScrimBuilder scheduledTomorrow() {
        this.scheduledTime = LocalDateTime.now().plusDays(1);
        return this;
    }

    public ScrimBuilder withLobby(Lobby lobby) {
        this.lobby = lobby;
        return this;
    }

    public ScrimBuilder withCustomLobby(String formatType) {
        this.lobby = new Lobby(formatType);
        return this;
    }

    public Scrim build() {
        validate();

        if (scheduledTime == null) {
            scheduledTime = LocalDateTime.now().plusMinutes(1);
        }

        Scrim scrim = new Scrim();
        scrim.setFormatType(formatType);
        scrim.setGame(game);
        scrim.setCreatedBy(createdBy);
        scrim.setMinTier(minTier);
        scrim.setMaxTier(maxTier);
        scrim.setRegion(region);
        scrim.setScheduledTime(scheduledTime);

        if (lobby == null) {
            lobby = new Lobby(formatType);
        }
        scrim.setLobby(lobby);

        scrim.setStatus(ScrimStatus.SEARCHING);
        scrim.setState(ScrimStateFactory.fromStatus(ScrimStatus.SEARCHING));

        return scrim;
    }

    private void validate() {
        List<String> errors = new ArrayList<>();

        if (formatType == null || formatType.trim().isEmpty()) {
            errors.add("Format type is required");
        }

        if (game == null) {
            errors.add("Game is required");
        }

        if (createdBy == null) {
            errors.add("Creator user is required");
        }

        if (scheduledTime != null && scheduledTime.isBefore(LocalDateTime.now())) {
            errors.add("Scheduled time cannot be in the past");
        }

        if (minTier != null && maxTier != null &&
                minTier.getRank() > maxTier.getRank()) {
            errors.add("Min tier cannot be higher than max tier");
        }

        if (!errors.isEmpty()) {
            throw new IllegalStateException("Invalid scrim configuration: " + String.join(", ", errors));
        }
    }
}