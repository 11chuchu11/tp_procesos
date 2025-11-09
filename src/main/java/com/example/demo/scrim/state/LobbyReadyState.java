package com.example.demo.scrim.state;

import com.example.demo.enums.ScrimStatus;
import com.example.demo.scrim.entity.Scrim;
import com.example.demo.scrim.factory.ScrimStateFactory;

public class LobbyReadyState implements ScrimState {

    @Override
    public String getStateName() {
        return "LOBBYREADY";
    }

    @Override
    public void apply(Scrim context) {
        unsupportedOperation("apply");
    }

    @Override
    public void cancel(Scrim context) {
        context.setState(ScrimStateFactory.fromStatus(ScrimStatus.CANCELLED));
        System.out.println("Scrim cancelled in lobby");
    }

    @Override
    public void finish(Scrim context) {
        unsupportedOperation("finish");
    }

    @Override
    public void lobbyFilled(Scrim context) {
        unsupportedOperation("lobby full");
    }

    @Override
    public void allPlayersConfirmed(Scrim context) {
        if (context.isScheduledTimeReached()) {
            context.setState(ScrimStateFactory.fromStatus(ScrimStatus.INGAME));
            System.out.println("All players confirmed and time reached, starting game");
        } else {
            context.setState(ScrimStateFactory.fromStatus(ScrimStatus.CONFIRMED));
            System.out.println("All players confirmed, waiting for scheduled time");
        }
    }

    @Override
    public void start(Scrim context) {
        unsupportedOperation("start");
    }
}
