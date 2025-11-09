package com.example.demo.scrim.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

public class CreateScrimRequest {

    @NotBlank(message = "Format type is required")
    @Pattern(regexp = "^(1v1|3v3|5v5)$", message = "Format type must be 1v1, 3v3, or 5v5")
    private String formatType;

    @NotNull(message = "Game ID is required")
    private Long gameId;

    private Long minTierId;

    private Long maxTierId;

    private Long regionId;

    private LocalDateTime scheduledTime;

    public CreateScrimRequest() {
    }

    public CreateScrimRequest(String formatType, Long gameId) {
        this.formatType = formatType;
        this.gameId = gameId;
    }

    public CreateScrimRequest(String formatType, Long gameId, Long minTierId, Long maxTierId, Long regionId, LocalDateTime scheduledTime) {
        this.formatType = formatType;
        this.gameId = gameId;
        this.minTierId = minTierId;
        this.maxTierId = maxTierId;
        this.regionId = regionId;
        this.scheduledTime = scheduledTime;
    }

    public String getFormatType() {
        return formatType;
    }

    public void setFormatType(String formatType) {
        this.formatType = formatType;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
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

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}

