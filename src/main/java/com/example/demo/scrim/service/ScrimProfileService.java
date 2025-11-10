package com.example.demo.scrim.service;

import com.example.demo.enums.ProfileStatus;
import com.example.demo.profile.entity.Profile;
import com.example.demo.profile.repository.ProfileRepository;
import com.example.demo.scrim.entity.Scrim;
import com.example.demo.team.entity.Team;
import com.example.demo.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScrimProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public Profile getProfileForUser(User user) {
        return profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + user.getUsername()));
    }

    public void markProfileAsBusy(Profile profile) {
        profile.setStatus(ProfileStatus.BUSY);
        profileRepository.save(profile);
    }

    public void markProfileAsAvailable(Profile profile) {
        profile.setStatus(ProfileStatus.AVAILABLE);
        profileRepository.save(profile);
    }

    public void releaseAllProfilesFromScrim(Scrim scrim) {
        if (scrim.getLobby() == null || scrim.getLobby().getTeams() == null) {
            return;
        }

        for (Team team : scrim.getLobby().getTeams()) {
            if (team.getPlayers() != null) {
                team.getPlayers().forEach(this::markProfileAsAvailable);
            }
        }
    }

    public List<Profile> getAllPlayersInLobby(Scrim scrim) {
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
}