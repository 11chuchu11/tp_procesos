package com.example.demo.scrim.state;

import com.example.demo.enums.ScrimStatus;
import com.example.demo.scrim.entity.Scrim;
import com.example.demo.scrim.factory.ScrimStateFactory;

public class SearchingState implements ScrimState {
    
    @Override
    public String getStateName() {
        return "SEARCHING";
    }

    @Override
    public void apply(Scrim context) {
        System.out.println("Player applied to scrim");
    }

    @Override
    public void cancel(Scrim context) {
        context.setState(ScrimStateFactory.fromStatus(ScrimStatus.CANCELLED));
        System.out.println("Scrim cancelled during search");
    }

    @Override
    public void finish(Scrim context) {
        unsupportedOperation("finish");
    }

    @Override
    public void lobbyFilled(Scrim context) {
        context.setState(ScrimStateFactory.fromStatus(ScrimStatus.LOBBYREADY));
        System.out.println("Lobby is now full! Moving to LOBBYREADY state");
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

