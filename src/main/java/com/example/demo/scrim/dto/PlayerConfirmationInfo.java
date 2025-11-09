package com.example.demo.scrim.dto;

import java.time.LocalDateTime;

public class PlayerConfirmationInfo {

    private Long profileId;
    private String username;
    private Boolean confirmed;
    private LocalDateTime confirmedAt;

    public PlayerConfirmationInfo() {
    }

    public PlayerConfirmationInfo(Long profileId, String username, Boolean confirmed, LocalDateTime confirmedAt) {
        this.profileId = profileId;
        this.username = username;
        this.confirmed = confirmed;
        this.confirmedAt = confirmedAt;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }
}

