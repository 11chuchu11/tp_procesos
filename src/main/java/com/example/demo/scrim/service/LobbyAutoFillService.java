package com.example.demo.scrim.service;

import com.example.demo.enums.ProfileStatus;
import com.example.demo.profile.entity.Profile;
import com.example.demo.profile.repository.ProfileRepository;
import com.example.demo.scrim.entity.Scrim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LobbyAutoFillService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ScrimProfileService profileService;

    public void autoFillLobby(Scrim scrim) {
        if (scrim.isLobbyFull()) {
            return;
        }

        List<Profile> eligibleProfiles = findEligibleProfiles(scrim);

        for (Profile profile : eligibleProfiles) {
            if (scrim.isLobbyFull()) {
                break;
            }

            boolean added = scrim.addPlayerToLobby(profile);
            if (added) {
                profileService.markProfileAsBusy(profile);
            }
        }
    }

    private List<Profile> findEligibleProfiles(Scrim scrim) {
        List<Profile> availableProfiles = profileRepository.findByMainGameAndStatus(
                scrim.getGame(),
                ProfileStatus.AVAILABLE);

        return availableProfiles.stream()
                .filter(scrim::isPlayerEligible)
                .filter(profile -> !scrim.isProfileInLobby(profile))
                .collect(Collectors.toList());
    }
}
