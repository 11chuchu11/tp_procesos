package com.example.demo.scrim.service;

import com.example.demo.game.entity.Game;
import com.example.demo.game.repository.GameRepository;
import com.example.demo.profile.entity.Profile;
import com.example.demo.profile.repository.ProfileRepository;
import com.example.demo.scrim.dto.ScrimResponse;
import com.example.demo.scrim.entity.Scrim;
import com.example.demo.scrim.repository.ScrimRepository;
import com.example.demo.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public List<ScrimResponse> getAllScrims() {
        return scrimRepository.findAll().stream()
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
    public ScrimResponse createScrim(String formatType, Long gameId) {
        // Get authenticated user from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new RuntimeException("User not authenticated");
        }
        
        User user = (User) authentication.getPrincipal();
        
        // Get user's profile
        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + user.getUsername()));

        // Validate game exists
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + gameId));

        // Create scrim with lobby and game
        Scrim scrim = new Scrim(formatType, game);
        
        // Auto-add creator to lobby
        boolean added = scrim.addPlayerToLobby(profile);
        if (!added) {
            throw new RuntimeException("Could not add creator to lobby");
        }

        Scrim savedScrim = scrimRepository.save(scrim);
        return toResponse(savedScrim);
    }

    @Transactional
    public ScrimResponse applyToScrim(Long scrimId) {
        // Get authenticated user from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new RuntimeException("User not authenticated");
        }
        
        User user = (User) authentication.getPrincipal();
        
        // Get user's profile
        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + user.getUsername()));

        Scrim scrim = scrimRepository.findById(scrimId)
                .orElseThrow(() -> new RuntimeException("Scrim not found with id: " + scrimId));

        // Validate profile is not already in lobby
        if (scrim.isProfileInLobby(profile)) {
            throw new RuntimeException("You are already registered in this scrim");
        }

        try {
            scrim.apply();
            boolean added = scrim.addPlayerToLobby(profile);
            
            if (!added) {
                throw new RuntimeException("Could not add player to lobby. Lobby might be full.");
            }

            Scrim savedScrim = scrimRepository.save(scrim);
            return toResponse(savedScrim);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Cannot apply to scrim: " + e.getMessage());
        }
    }

    @Transactional
    public ScrimResponse confirmScrim(Long scrimId) {
        Scrim scrim = scrimRepository.findById(scrimId)
                .orElseThrow(() -> new RuntimeException("Scrim not found with id: " + scrimId));

        try {
            // Here you would implement the confirm logic
            // For now, we'll just save and return
            Scrim savedScrim = scrimRepository.save(scrim);
            return toResponse(savedScrim);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Cannot confirm scrim: " + e.getMessage());
        }
    }

    @Transactional
    public ScrimResponse cancelScrim(Long scrimId) {
        Scrim scrim = scrimRepository.findById(scrimId)
                .orElseThrow(() -> new RuntimeException("Scrim not found with id: " + scrimId));

        try {
            scrim.cancel();
            Scrim savedScrim = scrimRepository.save(scrim);
            return toResponse(savedScrim);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Cannot cancel scrim: " + e.getMessage());
        }
    }

    @Transactional
    public ScrimResponse finishScrim(Long scrimId) {
        Scrim scrim = scrimRepository.findById(scrimId)
                .orElseThrow(() -> new RuntimeException("Scrim not found with id: " + scrimId));

        try {
            scrim.finish();
            Scrim savedScrim = scrimRepository.save(scrim);
            return toResponse(savedScrim);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Cannot finish scrim: " + e.getMessage());
        }
    }

    private ScrimResponse toResponse(Scrim scrim) {
        return new ScrimResponse(
                scrim.getScrimId(),
                scrim.getStatus(),
                scrim.getFormatType(),
                scrim.isLobbyFull(),
                scrim.getGame() != null ? scrim.getGame().getGameId() : null,
                scrim.getGame() != null ? scrim.getGame().getGameName() : null
        );
    }
}

