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
}

