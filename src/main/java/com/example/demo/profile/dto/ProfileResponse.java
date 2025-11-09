package com.example.demo.profile.dto;

import com.example.demo.enums.ProfileStatus;

public class ProfileResponse {

    private Long profileId;
    private Long userId;
    private String username;
    private Long mainGameId;
    private String mainGameName;
    private Long tierId;
    private String tierName;
    private Integer tierRank;
    private Long regionId;
    private String regionName;
    private ProfileStatus status;

    public ProfileResponse() {
    }

    public ProfileResponse(Long profileId, Long userId, String username, Long mainGameId, String mainGameName, 
                          Long tierId, String tierName, Integer tierRank, Long regionId, String regionName, ProfileStatus status) {
        this.profileId = profileId;
        this.userId = userId;
        this.username = username;
        this.mainGameId = mainGameId;
        this.mainGameName = mainGameName;
        this.tierId = tierId;
        this.tierName = tierName;
        this.tierRank = tierRank;
        this.regionId = regionId;
        this.regionName = regionName;
        this.status = status;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getMainGameId() {
        return mainGameId;
    }

    public void setMainGameId(Long mainGameId) {
        this.mainGameId = mainGameId;
    }

    public String getMainGameName() {
        return mainGameName;
    }

    public void setMainGameName(String mainGameName) {
        this.mainGameName = mainGameName;
    }

    public Long getTierId() {
        return tierId;
    }

    public void setTierId(Long tierId) {
        this.tierId = tierId;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public Integer getTierRank() {
        return tierRank;
    }

    public void setTierRank(Integer tierRank) {
        this.tierRank = tierRank;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public ProfileStatus getStatus() {
        return status;
    }

    public void setStatus(ProfileStatus status) {
        this.status = status;
    }
}

