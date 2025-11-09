package com.example.demo.scrim.dto;

import com.example.demo.enums.ScrimStatus;

public class ScrimSearchRequest {

    private Long gameId;
    private String formatType;
    private String region;
    private String minTier;
    private String maxTier;
    private ScrimStatus status;

    public ScrimSearchRequest() {
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getFormatType() {
        return formatType;
    }

    public void setFormatType(String formatType) {
        this.formatType = formatType;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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

    public ScrimStatus getStatus() {
        return status;
    }

    public void setStatus(ScrimStatus status) {
        this.status = status;
    }
}

