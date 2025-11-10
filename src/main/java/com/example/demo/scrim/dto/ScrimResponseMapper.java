package com.example.demo.scrim.dto;

import com.example.demo.enums.ScrimStatus;
import com.example.demo.scrim.entity.Scrim;
import com.example.demo.scrim.repository.PlayerConfirmationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScrimResponseMapper {

    @Autowired
    private PlayerConfirmationRepository confirmationRepository;

    public ScrimResponse toResponse(Scrim scrim) {
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
            enrichWithConfirmationData(scrim, response);
        }

        return response;
    }

    private void enrichWithConfirmationData(Scrim scrim, ScrimResponse response) {
        long totalPlayers = confirmationRepository.countByScrim(scrim);
        long confirmedPlayers = confirmationRepository.countByScrimAndConfirmed(scrim, true);
        response.setTotalPlayers((int) totalPlayers);
        response.setConfirmedPlayers((int) confirmedPlayers);
    }
}