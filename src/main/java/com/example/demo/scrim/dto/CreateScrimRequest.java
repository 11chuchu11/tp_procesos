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

    private String minTier;

    private String maxTier;

    private String region;

    private LocalDateTime scheduledTime;

    public CreateScrimRequest() {
    }

    public CreateScrimRequest(String formatType, Long gameId) {
        this.formatType = formatType;
        this.gameId = gameId;
    }

    public CreateScrimRequest(String formatType, Long gameId, String minTier, String maxTier, String region, LocalDateTime scheduledTime) {
        this.formatType = formatType;
        this.gameId = gameId;
        this.minTier = minTier;
        this.maxTier = maxTier;
        this.region = region;
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
}

