package com.example.demo.scrim.dto;

import com.example.demo.enums.ScrimStatus;

public class ScrimSearchRequest {

    private Long gameId;
    private String formatType;
    private Long regionId;
    private Long minTierId;
    private Long maxTierId;
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

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public Long getMinTierId() {
        return minTierId;
    }

    public void setMinTierId(Long minTierId) {
        this.minTierId = minTierId;
    }

    public Long getMaxTierId() {
        return maxTierId;
    }

    public void setMaxTierId(Long maxTierId) {
        this.maxTierId = maxTierId;
    }

    public ScrimStatus getStatus() {
        return status;
    }

    public void setStatus(ScrimStatus status) {
        this.status = status;
    }
}

