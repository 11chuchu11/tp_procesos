package com.example.demo.profile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateProfileRequest {

    @NotNull(message = "Main game ID is required")
    private Long mainGameId;

    @NotNull(message = "Tier ID is required")
    private Long tierId;

    @NotNull(message = "Region ID is required")
    private Long regionId;

    public CreateProfileRequest() {
    }

    public CreateProfileRequest(Long mainGameId, Long tierId, Long regionId) {
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
