package com.example.demo.scrim.service;

import com.example.demo.enums.ProfileStatus;
import com.example.demo.enums.ScrimStatus;
import com.example.demo.game.entity.Game;
import com.example.demo.game.repository.GameRepository;
import com.example.demo.profile.entity.Profile;
import com.example.demo.profile.repository.ProfileRepository;
import com.example.demo.region.entity.Region;
import com.example.demo.region.repository.RegionRepository;
import com.example.demo.scrim.dto.PlayerConfirmationInfo;
import com.example.demo.scrim.dto.ScrimResponse;
import com.example.demo.scrim.dto.ScrimSearchRequest;
import com.example.demo.scrim.entity.PlayerConfirmation;
import com.example.demo.scrim.entity.Scrim;
import com.example.demo.scrim.factory.ScrimStateFactory;
import com.example.demo.scrim.repository.PlayerConfirmationRepository;
import com.example.demo.scrim.repository.ScrimRepository;
import com.example.demo.scrim.specification.ScrimSpecification;
import com.example.demo.team.entity.Team;
import com.example.demo.tier.entity.Tier;
import com.example.demo.tier.repository.TierRepository;
import com.example.demo.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScrimService {

    @Autowired
    private ScrimRepository scrimRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TierRepository tierRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private PlayerConfirmationRepository confirmationRepository;

    @Transactional(readOnly = true)
    public List<ScrimResponse> getAllScrims() {
        return scrimRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ScrimResponse> searchScrims(ScrimSearchRequest searchRequest) {
        Specification<Scrim> spec = Specification.where(ScrimSpecification.hasGameId(searchRequest.getGameId()))
                .and(ScrimSpecification.hasFormatType(searchRequest.getFormatType()))
                .and(ScrimSpecification.hasRegionId(searchRequest.getRegionId()))
                .and(ScrimSpecification.hasMinTier(searchRequest.getMinTierId()))
                .and(ScrimSpecification.hasMaxTier(searchRequest.getMaxTierId()))
                .and(ScrimSpecification.hasStatus(searchRequest.getStatus()));

        return scrimRepository.findAll(spec).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ScrimResponse getScrimById(Long scrimId) {
        Scrim scrim = scrimRepository.findById(scrimId)
                .orElseThrow(() -> new RuntimeException("Scrim not found with id: " + scrimId));
        return toResponse(scrim);
    }

    @Transactional
    public ScrimResponse createScrim(String formatType, Long minTierId, Long maxTierId, Long regionId,
            LocalDateTime scheduledTime) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new RuntimeException("User not authenticated");
        }

        User user = (User) authentication.getPrincipal();

        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + user.getUsername()));

        if (profile.getStatus() != ProfileStatus.AVAILABLE) {
            throw new RuntimeException("You are already in another scrim or busy.");
        }

        Long gameId = profile.getMainGame().getGameId();

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + gameId));

        Tier minTier = null;
        if (minTierId != null) {
            minTier = tierRepository.findById(minTierId)
                    .orElseThrow(() -> new RuntimeException("Min tier not found with id: " + minTierId));

            if (!minTier.getGame().getGameId().equals(gameId)) {
                throw new RuntimeException("Min tier does not belong to the selected game");
            }
        }

        Tier maxTier = null;
        if (maxTierId != null) {
            maxTier = tierRepository.findById(maxTierId)
                    .orElseThrow(() -> new RuntimeException("Max tier not found with id: " + maxTierId));

            if (!maxTier.getGame().getGameId().equals(gameId)) {
                throw new RuntimeException("Max tier does not belong to the selected game");
            }
        }

        if (minTier != null && maxTier != null && minTier.getRank() > maxTier.getRank()) {
            throw new RuntimeException("Min tier cannot be higher than max tier");
        }

        Region region = null;
        if (regionId != null) {
            region = regionRepository.findById(regionId)
                    .orElseThrow(() -> new RuntimeException("Region not found with id: " + regionId));
        }

        if (scheduledTime == null) {
            scheduledTime = LocalDateTime.now().plusHours(1);
        }

        Scrim scrim = new Scrim(formatType, game, user, minTier, maxTier, region, scheduledTime);

        if (!scrim.isPlayerEligible(profile)) {
            throw new RuntimeException(
                    "Your tier (" + profile.getMainTier().getTierName()
                            + ") is not within the required range for this scrim");
        }

        boolean added = scrim.addPlayerToLobby(profile);
        if (!added) {
            throw new RuntimeException("Could not add creator to lobby");
        }

        profile.setStatus(ProfileStatus.BUSY);
        profileRepository.save(profile);

        Scrim savedScrim = scrimRepository.save(scrim);

        autoFillLobby(savedScrim);

        savedScrim = scrimRepository.save(savedScrim);

        if (savedScrim.isLobbyFull()) {
            savedScrim.setState(ScrimStateFactory.fromStatus(ScrimStatus.LOBBYREADY));
            savedScrim = scrimRepository.save(savedScrim);
            createConfirmationRecords(savedScrim);
        }

        return toResponse(savedScrim);
    }

    @Transactional
    public ScrimResponse applyToScrim(Long scrimId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new RuntimeException("User not authenticated");
        }

        User user = (User) authentication.getPrincipal();

        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + user.getUsername()));

        if (profile.getStatus() != ProfileStatus.AVAILABLE) {
            throw new RuntimeException("You are already in another scrim or busy.");
        }

        Scrim scrim = scrimRepository.findById(scrimId)
                .orElseThrow(() -> new RuntimeException("Scrim not found with id: " + scrimId));

        if (scrim.getStatus() == ScrimStatus.CANCELLED || scrim.getStatus() == ScrimStatus.FINISHED
                || scrim.getStatus() == ScrimStatus.INGAME) {
            throw new RuntimeException("Cannot apply to a cancelled, finished or in-game scrim");
        }

        if (!profile.getMainGame().getGameId().equals(scrim.getGame().getGameId())) {
            throw new RuntimeException(
                    "Your profile is for " + profile.getMainGame().getGameName()
                            + " but this scrim is for " + scrim.getGame().getGameName());
        }

        if (!scrim.isPlayerEligible(profile)) {
            throw new RuntimeException(
                    "Your tier (" + profile.getMainTier().getTierName() + ") does not meet the requirements.");
        }

        try {
            scrim.apply();
            boolean added = scrim.addPlayerToLobby(profile);

            if (!added) {
                throw new RuntimeException("Could not add player to lobby. Lobby might be full.");
            }

            profile.setStatus(ProfileStatus.BUSY);
            profileRepository.save(profile);

            Scrim savedScrim = scrimRepository.save(scrim);
            return toResponse(savedScrim);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Cannot apply to scrim: " + e.getMessage());
        }
    }

    @Transactional
    public ScrimResponse confirmParticipation(Long scrimId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new RuntimeException("User not authenticated");
        }

        User user = (User) authentication.getPrincipal();

        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + user.getUsername()));

        Scrim scrim = scrimRepository.findById(scrimId)
                .orElseThrow(() -> new RuntimeException("Scrim not found with id: " + scrimId));

        if (scrim.getStatus() != ScrimStatus.LOBBYREADY) {
            throw new RuntimeException("Scrim is not ready for confirmations. Current status: " + scrim.getStatus());
        }

        PlayerConfirmation confirmation = confirmationRepository.findByScrimAndProfile(scrim, profile)
                .orElseThrow(() -> new RuntimeException("You are not in this scrim"));

        if (confirmation.getConfirmed()) {
            throw new RuntimeException("You have already confirmed");
        }

        confirmation.setConfirmed(true);
        confirmation.setConfirmedAt(LocalDateTime.now());
        confirmationRepository.save(confirmation);

        if (allPlayersConfirmed(scrim)) {
            if (scrim.isScheduledTimeReached()) {
                scrim.setState(ScrimStateFactory.fromStatus(ScrimStatus.INGAME));
            } else {
                scrim.setState(ScrimStateFactory.fromStatus(ScrimStatus.CONFIRMED));
            }
            scrim = scrimRepository.save(scrim);
        }

        return toResponse(scrim);
    }

    @Transactional(readOnly = true)
    public List<PlayerConfirmationInfo> getConfirmations(Long scrimId) {
        Scrim scrim = scrimRepository.findById(scrimId)
                .orElseThrow(() -> new RuntimeException("Scrim not found with id: " + scrimId));

        List<PlayerConfirmation> confirmations = confirmationRepository.findByScrim(scrim);

        return confirmations.stream()
                .map(c -> new PlayerConfirmationInfo(
                        c.getProfile().getProfileId(),
                        c.getProfile().getUser().getUsername(),
                        c.getConfirmed(),
                        c.getConfirmedAt()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ScrimResponse cancelScrim(Long scrimId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new RuntimeException("User not authenticated");
        }

        User user = (User) authentication.getPrincipal();

        Scrim scrim = scrimRepository.findById(scrimId)
                .orElseThrow(() -> new RuntimeException("Scrim not found with id: " + scrimId));

        if (!scrim.getCreatedBy().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Only the creator can cancel this scrim");
        }

        try {
            scrim.cancel();
            releaseProfilesFromScrim(scrim);
            Scrim savedScrim = scrimRepository.save(scrim);
            return toResponse(savedScrim);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Cannot cancel scrim: " + e.getMessage());
        }
    }

    @Transactional
    public ScrimResponse startScrim(Long scrimId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new RuntimeException("User not authenticated");
        }

        User user = (User) authentication.getPrincipal();

        Scrim scrim = scrimRepository.findById(scrimId)
                .orElseThrow(() -> new RuntimeException("Scrim not found with id: " + scrimId));

        if (!scrim.getCreatedBy().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Only the creator can start the scrim");
        }

        if (scrim.getStatus() != ScrimStatus.CONFIRMED) {
            throw new RuntimeException(
                    "Scrim must be in CONFIRMED state to start. Current status: " + scrim.getStatus());
        }

        scrim.setState(ScrimStateFactory.fromStatus(ScrimStatus.INGAME));
        scrim = scrimRepository.save(scrim);

        return toResponse(scrim);
    }

    @Transactional
    public ScrimResponse finishScrim(Long scrimId) {
        Scrim scrim = scrimRepository.findById(scrimId)
                .orElseThrow(() -> new RuntimeException("Scrim not found with id: " + scrimId));

        try {
            scrim.finish();
            releaseProfilesFromScrim(scrim);
            Scrim savedScrim = scrimRepository.save(scrim);
            return toResponse(savedScrim);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Cannot finish scrim: " + e.getMessage());
        }
    }

    private void autoFillLobby(Scrim scrim) {
        if (scrim.isLobbyFull()) {
            return;
        }

        List<Profile> availableProfiles = profileRepository.findByMainGameAndStatus(
                scrim.getGame(),
                ProfileStatus.AVAILABLE);

        List<Profile> eligibleProfiles = availableProfiles.stream()
                .filter(scrim::isPlayerEligible)
                .filter(profile -> !scrim.isProfileInLobby(profile))
                .collect(Collectors.toList());

        for (Profile profile : eligibleProfiles) {
            if (scrim.isLobbyFull()) {
                break;
            }

            boolean added = scrim.addPlayerToLobby(profile);
            if (added) {
                profile.setStatus(ProfileStatus.BUSY);
                profileRepository.save(profile);
            }
        }
    }

    private void releaseProfilesFromScrim(Scrim scrim) {
        if (scrim.getLobby() == null || scrim.getLobby().getTeams() == null) {
            return;
        }

        for (Team team : scrim.getLobby().getTeams()) {
            if (team.getPlayers() != null) {
                for (Profile profile : team.getPlayers()) {
                    profile.setStatus(ProfileStatus.AVAILABLE);
                    profileRepository.save(profile);
                }
            }
        }
    }

    private void createConfirmationRecords(Scrim scrim) {
        List<Profile> players = getAllPlayersInLobby(scrim);

        for (Profile player : players) {
            if (!confirmationRepository.existsByScrimAndProfile(scrim, player)) {
                PlayerConfirmation confirmation = new PlayerConfirmation(scrim, player);
                confirmationRepository.save(confirmation);
            }
        }
    }

    private List<Profile> getAllPlayersInLobby(Scrim scrim) {
        List<Profile> players = new ArrayList<>();
        if (scrim.getLobby() != null && scrim.getLobby().getTeams() != null) {
            for (Team team : scrim.getLobby().getTeams()) {
                if (team.getPlayers() != null) {
                    players.addAll(team.getPlayers());
                }
            }
        }
        return players;
    }

    private boolean allPlayersConfirmed(Scrim scrim) {
        long totalPlayers = confirmationRepository.countByScrim(scrim);
        long confirmedPlayers = confirmationRepository.countByScrimAndConfirmed(scrim, true);

        return totalPlayers > 0 && totalPlayers == confirmedPlayers;
    }

    private ScrimResponse toResponse(Scrim scrim) {
        ScrimResponse response = new ScrimResponse(
                scrim.getScrimId(),
                scrim.getStatus(),
                scrim.getFormatType(),
                scrim.isLobbyFull(),
                scrim.getGame() != null ? scrim.getGame().getGameId() : null,
                scrim.getGame() != null ? scrim.getGame().getGameName() : null,
                scrim.getMinTier() != null ? scrim.getMinTier().getTierId() : null,
                scrim.getMinTier() != null ? scrim.getMinTier().getTierName() : null,
                scrim.getMaxTier() != null ? scrim.getMaxTier().getTierId() : null,
                scrim.getMaxTier() != null ? scrim.getMaxTier().getTierName() : null,
                scrim.getRegion() != null ? scrim.getRegion().getRegionId() : null,
                scrim.getRegion() != null ? scrim.getRegion().getRegionName() : null);

        response.setScheduledTime(scrim.getScheduledTime());

        if (scrim.getStatus() == ScrimStatus.LOBBYREADY || scrim.getStatus() == ScrimStatus.CONFIRMED) {
            long totalPlayers = confirmationRepository.countByScrim(scrim);
            long confirmedPlayers = confirmationRepository.countByScrimAndConfirmed(scrim, true);
            response.setTotalPlayers((int) totalPlayers);
            response.setConfirmedPlayers((int) confirmedPlayers);
        }

        return response;
    }
}
