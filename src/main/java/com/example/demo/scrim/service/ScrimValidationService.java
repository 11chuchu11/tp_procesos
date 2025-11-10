package com.example.demo.scrim.service;

import com.example.demo.enums.ProfileStatus;
import com.example.demo.enums.ScrimStatus;
import com.example.demo.profile.entity.Profile;
import com.example.demo.region.entity.Region;
import com.example.demo.region.repository.RegionRepository;
import com.example.demo.scrim.entity.Scrim;
import com.example.demo.tier.entity.Tier;
import com.example.demo.tier.repository.TierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScrimValidationService {

    @Autowired
    private TierRepository tierRepository;

    @Autowired
    private RegionRepository regionRepository;

    public Tier validateAndGetTier(Long tierId, Long gameId, String tierType) {
        if (tierId == null) return null;

        Tier tier = tierRepository.findById(tierId)
                .orElseThrow(() -> new RuntimeException(tierType + " tier not found"));

        if (!tier.getGame().getGameId().equals(gameId)) {
            throw new RuntimeException(tierType + " tier does not belong to the selected game");
        }

        return tier;
    }

    public void validateTierRange(Tier minTier, Tier maxTier) {
        if (minTier != null && maxTier != null && minTier.getRank() > maxTier.getRank()) {
            throw new RuntimeException("Min tier cannot be higher than max tier");
        }
    }

    public Region validateAndGetRegion(Long regionId) {
        if (regionId == null) return null;
        return regionRepository.findById(regionId)
                .orElseThrow(() -> new RuntimeException("Region not found with id: " + regionId));
    }

    public void validateProfileEligibility(Scrim scrim, Profile profile) {
        if (!scrim.isPlayerEligible(profile)) {
            throw new RuntimeException(
                    "Your tier (" + profile.getMainTier().getTierName() +
                            ") is not within the required range for this scrim");
        }
    }

    public void validateProfileAvailability(Profile profile) {
        if (profile.getStatus() != ProfileStatus.AVAILABLE) {
            throw new RuntimeException("You are already in another scrim or busy.");
        }
    }

    public void validateScrimApplicable(Scrim scrim) {
        if (scrim.getStatus() == ScrimStatus.CANCELLED ||
                scrim.getStatus() == ScrimStatus.FINISHED ||
                scrim.getStatus() == ScrimStatus.INGAME) {
            throw new RuntimeException("Cannot apply to a cancelled, finished or in-game scrim");
        }
    }
}