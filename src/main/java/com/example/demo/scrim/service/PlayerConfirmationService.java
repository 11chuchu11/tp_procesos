package com.example.demo.scrim.service;

import com.example.demo.profile.entity.Profile;
import com.example.demo.scrim.dto.PlayerConfirmationInfo;
import com.example.demo.scrim.entity.PlayerConfirmation;
import com.example.demo.scrim.entity.Scrim;
import com.example.demo.scrim.repository.PlayerConfirmationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerConfirmationService {

    @Autowired
    private PlayerConfirmationRepository confirmationRepository;

    @Autowired
    private ScrimProfileService profileService;

    public void createConfirmationRecords(Scrim scrim) {
        List<Profile> players = profileService.getAllPlayersInLobby(scrim);

        for (Profile player : players) {
            if (!confirmationRepository.existsByScrimAndProfile(scrim, player)) {
                PlayerConfirmation confirmation = new PlayerConfirmation(scrim, player);
                confirmationRepository.save(confirmation);
            }
        }
    }

    public boolean allPlayersConfirmed(Scrim scrim) {
        long totalPlayers = confirmationRepository.countByScrim(scrim);
        long confirmedPlayers = confirmationRepository.countByScrimAndConfirmed(scrim, true);
        return totalPlayers > 0 && totalPlayers == confirmedPlayers;
    }

    public PlayerConfirmation confirmPlayerForScrim(Scrim scrim, Profile profile) {
        PlayerConfirmation confirmation = confirmationRepository.findByScrimAndProfile(scrim, profile)
                .orElseThrow(() -> new RuntimeException("You are not in this scrim"));

        if (confirmation.getConfirmed()) {
            throw new RuntimeException("You have already confirmed");
        }

        confirmation.setConfirmed(true);
        confirmation.setConfirmedAt(LocalDateTime.now());
        return confirmationRepository.save(confirmation);
    }

    public List<PlayerConfirmationInfo> getConfirmationsForScrim(Scrim scrim) {
        List<PlayerConfirmation> confirmations = confirmationRepository.findByScrim(scrim);

        return confirmations.stream()
                .map(c -> new PlayerConfirmationInfo(
                        c.getProfile().getProfileId(),
                        c.getProfile().getUser().getUsername(),
                        c.getConfirmed(),
                        c.getConfirmedAt()))
                .collect(Collectors.toList());
    }
}
