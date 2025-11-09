package com.example.demo.scrim.state;

import com.example.demo.enums.ScrimStatus;
import com.example.demo.scrim.entity.Scrim;
import com.example.demo.scrim.factory.ScrimStateFactory;

public class InGameState implements ScrimState {

    @Override
    public String getStateName() {
        return "INGAME";
    }

    @Override
    public void apply(Scrim context) {
        unsupportedOperation("apply");
    }

    @Override
    public void cancel(Scrim context) {
        unsupportedOperation("cancel");
    }

    @Override
    public void finish(Scrim context) {
        context.setState(ScrimStateFactory.fromStatus(ScrimStatus.FINISHED));
        System.out.println("Scrim finished successfully");
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
        unsupportedOperation("start");
    }
}
