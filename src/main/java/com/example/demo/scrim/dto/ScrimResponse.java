package com.example.demo.scrim.dto;

import com.example.demo.enums.ScrimStatus;
import java.time.LocalDateTime;

public class ScrimResponse {

    private Long scrimId;
    private ScrimStatus status;
    private String formatType;
    private boolean lobbyFull;
    private Long gameId;
    private String gameName;
    private String minTier;
    private String maxTier;
    private String region;
    private Integer totalPlayers;
    private Integer confirmedPlayers;
    private LocalDateTime scheduledTime;

    public ScrimResponse() {
    }

    public ScrimResponse(Long scrimId, ScrimStatus status, String formatType, boolean lobbyFull, Long gameId, String gameName, String minTier, String maxTier, String region) {
        this.scrimId = scrimId;
        this.status = status;
        this.formatType = formatType;
        this.lobbyFull = lobbyFull;
        this.gameId = gameId;
        this.gameName = gameName;
        this.minTier = minTier;
        this.maxTier = maxTier;
        this.region = region;
    }

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

    public String getFormatType() {
        return formatType;
    }

    public void setFormatType(String formatType) {
        this.formatType = formatType;
    }

    public boolean isLobbyFull() {
        return lobbyFull;
    }

    public void setLobbyFull(boolean lobbyFull) {
        this.lobbyFull = lobbyFull;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
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

    public Integer getTotalPlayers() {
        return totalPlayers;
    }

    public void setTotalPlayers(Integer totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public Integer getConfirmedPlayers() {
        return confirmedPlayers;
    }

    public void setConfirmedPlayers(Integer confirmedPlayers) {
        this.confirmedPlayers = confirmedPlayers;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}

