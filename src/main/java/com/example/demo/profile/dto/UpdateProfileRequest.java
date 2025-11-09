package com.example.demo.profile.dto;

public class UpdateProfileRequest {

    private Long mainGameId;
    private Long tierId;
    private Long regionId;

    public UpdateProfileRequest() {
    }

    public UpdateProfileRequest(Long mainGameId, Long tierId, Long regionId) {
        this.mainGameId = mainGameId;
        this.tierId = tierId;
        this.regionId = regionId;
    }

    public Long getMainGameId() {
        return mainGameId;
    }

    public void setMainGameId(Long mainGameId) {
        this.mainGameId = mainGameId;
    }

    public Long getTierId() {
        return tierId;
    }

    public void setTierId(Long tierId) {
        this.tierId = tierId;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }
}

