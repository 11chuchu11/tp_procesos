package com.example.demo.scrim.dto;

import com.example.demo.enums.ScrimStatus;

public class ScrimResponse {

    private Long scrimId;
    private ScrimStatus status;
    private String formatType;
    private boolean lobbyFull;
    private Long gameId;
    private String gameName;

    public ScrimResponse() {
    }

    public ScrimResponse(Long scrimId, ScrimStatus status, String formatType, boolean lobbyFull, Long gameId, String gameName) {
        this.scrimId = scrimId;
        this.status = status;
        this.formatType = formatType;
        this.lobbyFull = lobbyFull;
        this.gameId = gameId;
        this.gameName = gameName;
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
}

