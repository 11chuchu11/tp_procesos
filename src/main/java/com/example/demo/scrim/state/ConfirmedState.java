package com.example.demo.scrim.state;

import com.example.demo.enums.ScrimStatus;
import com.example.demo.scrim.entity.Scrim;
import com.example.demo.scrim.factory.ScrimStateFactory;

public class ConfirmedState implements ScrimState {

    @Override
    public String getStateName() {
        return "CONFIRMED";
    }

    @Override
    public void apply(Scrim context) {
        unsupportedOperation("apply");
    }

    @Override
    public void cancel(Scrim context) {
        context.setState(ScrimStateFactory.fromStatus(ScrimStatus.CANCELLED));
        System.out.println("Confirmed scrim cancelled");
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
        unsupportedOperation("confirm players");
    }

    @Override
    public void start(Scrim context) {
        context.setState(ScrimStateFactory.fromStatus(ScrimStatus.INGAME));
        System.out.println("Starting scrim, moving to INGAME state");
    }
}
