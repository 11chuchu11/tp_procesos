package com.example.demo.scrim.factory;

import com.example.demo.enums.ScrimStatus;
import com.example.demo.scrim.state.*;

public class ScrimStateFactory {

    private ScrimStateFactory() {}

    public static ScrimState fromStatus(ScrimStatus status) {
        return switch (status) {
            case CONFIRMED -> new ConfirmedState();
            case FINISHED -> new FinishedState();
            case INGAME -> new InGameState();
            case LOBBYREADY -> new LobbyReadyState();
            case CANCELLED -> new CancelledState();
            default -> new SearchingState();
        };
    }

    public static ScrimState fromString(String statusName) {
        return fromStatus(ScrimStatus.valueOf(statusName));
    }
}