package com.example.demo.scrim.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CreateScrimRequest {

    @NotBlank(message = "Format type is required")
    @Pattern(regexp = "^(1v1|3v3|5v5)$", message = "Format type must be 1v1, 3v3, or 5v5")
    private String formatType;

    @NotNull(message = "Game ID is required")
    private Long gameId;

    public CreateScrimRequest() {
    }

    public CreateScrimRequest(String formatType, Long gameId) {
        this.formatType = formatType;
        this.gameId = gameId;
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
}

