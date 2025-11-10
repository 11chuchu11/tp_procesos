package com.example.demo.scrim.service;

import com.example.demo.enums.ScrimStatus;
import com.example.demo.game.entity.Game;
import com.example.demo.game.repository.GameRepository;
import com.example.demo.profile.entity.Profile;
import com.example.demo.region.entity.Region;
import com.example.demo.schedule.ScrimSchedulerService;
import com.example.demo.scrim.dto.PlayerConfirmationInfo;
import com.example.demo.scrim.dto.ScrimResponse;
import com.example.demo.scrim.dto.ScrimResponseMapper;
import com.example.demo.scrim.dto.ScrimSearchRequest;
import com.example.demo.scrim.entity.Scrim;
import com.example.demo.scrim.repository.ScrimRepository;
import com.example.demo.scrim.specification.ScrimSpecification;
import com.example.demo.tier.entity.Tier;
import com.example.demo.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScrimService {

    @Autowired
    private ScrimRepository scrimRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ScrimSchedulerService scrimSchedulerService;

    @Autowired
    private ScrimAuthorizationService authService;

    @Autowired
    private ScrimValidationService validationService;

    @Autowired
    private ScrimProfileService profileService;

    @Autowired
    private PlayerConfirmationService confirmationService;

    @Autowired
    private LobbyAutoFillService autoFillService;

    @Autowired
    private ScrimResponseMapper responseMapper;

    @Transactional(readOnly = true)
    public List<ScrimResponse> getAllScrims() {
        return scrimRepository.findAll().stream()
                .map(responseMapper::toResponse)
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
                .map(responseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ScrimResponse getScrimById(Long scrimId) {
        Scrim scrim = findScrimById(scrimId);
        return responseMapper.toResponse(scrim);
    }

    @Transactional
    public ScrimResponse createScrim(String formatType, Long minTierId, Long maxTierId, Long regionId,
            LocalDateTime scheduledTime) {
        User user = authService.getCurrentAuthenticatedUser();
        Profile profile = profileService.getProfileForUser(user);

        validationService.validateProfileAvailability(profile);

        Long gameId = profile.getMainGame().getGameId();
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        Tier minTier = validationService.validateAndGetTier(minTierId, gameId, "Min");
        Tier maxTier = validationService.validateAndGetTier(maxTierId, gameId, "Max");
        validationService.validateTierRange(minTier, maxTier);

        Region region = validationService.validateAndGetRegion(regionId);

        if (scheduledTime == null) {
            scheduledTime = LocalDateTime.now().plusMinutes(1);
        }

        Scrim scrim = new Scrim(formatType, game, user, minTier, maxTier, region, scheduledTime);

        validationService.validateProfileEligibility(scrim, profile);

        addCreatorToScrim(scrim, profile);

        Scrim savedScrim = scrimRepository.save(scrim);
        autoFillService.autoFillLobby(savedScrim);
        savedScrim = scrimRepository.save(savedScrim);

        scrimSchedulerService.scheduleScrim(savedScrim.getScrimId());
        handleLobbyFilledIfNeeded(savedScrim);

        return responseMapper.toResponse(savedScrim);
    }

    @Transactional
    public ScrimResponse applyToScrim(Long scrimId) {
        User user = authService.getCurrentAuthenticatedUser();
        Profile profile = profileService.getProfileForUser(user);

        validationService.validateProfileAvailability(profile);

        Scrim scrim = findScrimById(scrimId);

        validationService.validateScrimApplicable(scrim);

        if (!profile.getMainGame().getGameId().equals(scrim.getGame().getGameId())) {
            throw new RuntimeException(
                    "Your profile is for " + profile.getMainGame().getGameName()
                            + " but this scrim is for " + scrim.getGame().getGameName());
        }

        validationService.validateProfileEligibility(scrim, profile);

        try {
            scrim.apply();
            boolean added = scrim.addPlayerToLobby(profile);

            if (!added) {
                throw new RuntimeException("Could not add player to lobby. Lobby might be full.");
            }

            profileService.markProfileAsBusy(profile);

            Scrim savedScrim = scrimRepository.save(scrim);
            return responseMapper.toResponse(savedScrim);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Cannot apply to scrim: " + e.getMessage());
        }
    }

    @Transactional
    public ScrimResponse confirmParticipation(Long scrimId) {
        User user = authService.getCurrentAuthenticatedUser();
        Profile profile = profileService.getProfileForUser(user);
        Scrim scrim = findScrimById(scrimId);

        if (scrim.getStatus() != ScrimStatus.LOBBYREADY) {
            throw new RuntimeException("Scrim is not ready for confirmations. Current status: " + scrim.getStatus());
        }

        confirmationService.confirmPlayerForScrim(scrim, profile);

        if (confirmationService.allPlayersConfirmed(scrim)) {
            scrim.allPlayersConfirmed();
            scrim = scrimRepository.save(scrim);
        }

        return responseMapper.toResponse(scrim);
    }

    @Transactional(readOnly = true)
    public List<PlayerConfirmationInfo> getConfirmations(Long scrimId) {
        Scrim scrim = findScrimById(scrimId);
        return confirmationService.getConfirmationsForScrim(scrim);
    }

    @Transactional
    public ScrimResponse cancelScrim(Long scrimId) {
        User user = authService.getCurrentAuthenticatedUser();
        Scrim scrim = findScrimById(scrimId);

        authService.validateScrimOwnership(scrim, user);

        scrimSchedulerService.cancelScheduledTask(scrim.getScrimId());

        try {
            scrim.cancel();
            profileService.releaseAllProfilesFromScrim(scrim);
            Scrim savedScrim = scrimRepository.save(scrim);
            return responseMapper.toResponse(savedScrim);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Cannot cancel scrim: " + e.getMessage());
        }
    }

    @Transactional
    public ScrimResponse startScrim(Long scrimId) {
        User user = authService.getCurrentAuthenticatedUser();
        Scrim scrim = findScrimById(scrimId);

        authService.validateScrimOwnership(scrim, user);

        if (scrim.getStatus() != ScrimStatus.CONFIRMED) {
            throw new RuntimeException(
                    "Scrim must be in CONFIRMED state to start. Current status: " + scrim.getStatus());
        }

        try {
            scrim.start();
            scrim = scrimRepository.save(scrim);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Cannot start scrim: " + e.getMessage());
        }

        return responseMapper.toResponse(scrim);
    }

    @Transactional
    public void internalStartScrim(Long scrimId) {
        Scrim scrim = findScrimById(scrimId);

        if (scrim.getStatus() != ScrimStatus.CONFIRMED) {
            throw new RuntimeException(
                    "Scrim must be in CONFIRMED state to start. Current status: " + scrim.getStatus());
        }

        try {
            scrim.start();
            scrimRepository.save(scrim);
            System.out.println("✅ Scrim " + scrimId + " iniciado automáticamente");
        } catch (IllegalStateException e) {
            throw new RuntimeException("Cannot start scrim: " + e.getMessage());
        }
    }

    @Transactional
    public void internalCancelScrim(Long scrimId) {
        Scrim scrim = findScrimById(scrimId);

        try {
            scrim.cancel();
            profileService.releaseAllProfilesFromScrim(scrim);
            scrimRepository.save(scrim);
            System.out.println("❌ Scrim " + scrimId + " cancelado automáticamente");
        } catch (IllegalStateException e) {
            throw new RuntimeException("Cannot cancel scrim: " + e.getMessage());
        }
    }

    @Transactional
    public ScrimResponse finishScrim(Long scrimId) {
        Scrim scrim = findScrimById(scrimId);

        try {
            scrim.finish();
            profileService.releaseAllProfilesFromScrim(scrim);
            Scrim savedScrim = scrimRepository.save(scrim);
            return responseMapper.toResponse(savedScrim);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Cannot finish scrim: " + e.getMessage());
        }
    }

    // Helper methods

    private Scrim findScrimById(Long scrimId) {
        return scrimRepository.findById(scrimId)
                .orElseThrow(() -> new RuntimeException("Scrim not found with id: " + scrimId));
    }

    private void addCreatorToScrim(Scrim scrim, Profile profile) {
        boolean added = scrim.addPlayerToLobby(profile);
        if (!added) {
            throw new RuntimeException("Could not add creator to lobby");
        }
        profileService.markProfileAsBusy(profile);
    }

    private void handleLobbyFilledIfNeeded(Scrim scrim) {
        if (scrim.isLobbyFull() && scrim.getStatus() == ScrimStatus.LOBBYREADY) {
            confirmationService.createConfirmationRecords(scrim);
        } else if (scrim.isLobbyFull() && scrim.getStatus() == ScrimStatus.SEARCHING) {
            scrim.lobbyFilled();
            scrimRepository.save(scrim);
            confirmationService.createConfirmationRecords(scrim);
        }
    }
}
