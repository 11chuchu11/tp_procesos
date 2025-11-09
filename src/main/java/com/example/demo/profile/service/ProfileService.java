package com.example.demo.profile.service;

import com.example.demo.enums.ProfileStatus;
import com.example.demo.game.entity.Game;
import com.example.demo.game.repository.GameRepository;
import com.example.demo.profile.dto.CreateProfileRequest;
import com.example.demo.profile.dto.ProfileResponse;
import com.example.demo.profile.dto.UpdateProfileRequest;
import com.example.demo.profile.entity.Profile;
import com.example.demo.profile.repository.ProfileRepository;
import com.example.demo.region.entity.Region;
import com.example.demo.region.repository.RegionRepository;
import com.example.demo.tier.entity.Tier;
import com.example.demo.tier.repository.TierRepository;
import com.example.demo.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TierRepository tierRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Transactional
    public ProfileResponse createProfile(CreateProfileRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new RuntimeException("User not authenticated");
        }

        User user = (User) authentication.getPrincipal();

        if (profileRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("Profile already exists for this user");
        }

        Game game = gameRepository.findById(request.getMainGameId())
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + request.getMainGameId()));

        Tier tier = tierRepository.findById(request.getTierId())
                .orElseThrow(() -> new RuntimeException("Tier not found with id: " + request.getTierId()));

        if (!tier.getGame().getGameId().equals(game.getGameId())) {
            throw new RuntimeException("Tier does not belong to the selected game");
        }

        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new RuntimeException("Region not found with id: " + request.getRegionId()));

        Profile profile = new Profile(user, game, tier, region);
        Profile savedProfile = profileRepository.save(profile);

        return toResponse(savedProfile);
    }

    @Transactional
    public ProfileResponse updateProfile(UpdateProfileRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new RuntimeException("User not authenticated");
        }

        User user = (User) authentication.getPrincipal();

        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + user.getUsername()));

        if (profile.getStatus() == ProfileStatus.BUSY) {
            throw new RuntimeException("Cannot update profile while in a scrim. Status: " + profile.getStatus());
        }

        if (request.getMainGameId() != null) {
            Game game = gameRepository.findById(request.getMainGameId())
                    .orElseThrow(() -> new RuntimeException("Game not found with id: " + request.getMainGameId()));
            profile.setMainGame(game);
        }

        if (request.getTierId() != null) {
            Tier tier = tierRepository.findById(request.getTierId())
                    .orElseThrow(() -> new RuntimeException("Tier not found with id: " + request.getTierId()));
            
            if (!tier.getGame().getGameId().equals(profile.getMainGame().getGameId())) {
                throw new RuntimeException("Tier does not belong to the selected game");
            }
            
            profile.setMainTier(tier);
        }

        if (request.getRegionId() != null) {
            Region region = regionRepository.findById(request.getRegionId())
                    .orElseThrow(() -> new RuntimeException("Region not found with id: " + request.getRegionId()));
            profile.setRegion(region);
        }

        Profile updatedProfile = profileRepository.save(profile);

        return toResponse(updatedProfile);
    }

    @Transactional(readOnly = true)
    public ProfileResponse getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new RuntimeException("User not authenticated");
        }

        User user = (User) authentication.getPrincipal();

        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + user.getUsername()));

        return toResponse(profile);
    }

    private ProfileResponse toResponse(Profile profile) {
        return new ProfileResponse(
                profile.getProfileId(),
                profile.getUser().getUserId(),
                profile.getUser().getUsername(),
                profile.getMainGame().getGameId(),
                profile.getMainGame().getGameName(),
                profile.getMainTier().getTierId(),
                profile.getMainTier().getTierName(),
                profile.getMainTier().getRank(),
                profile.getRegion() != null ? profile.getRegion().getRegionId() : null,
                profile.getRegion() != null ? profile.getRegion().getRegionName() : null,
                profile.getStatus()
        );
    }
}

