package com.example.demo.scrim.state;

import com.example.demo.scrim.entity.Scrim;

public class LobbyReadyState implements ScrimState {
    
    @Override
    public String getStateName() {
        return "LOBBYREADY";
    }

    @Override
    public void handle(Scrim context) {
        // Lógica específica del estado LobbyReady
        // Transición al siguiente estado si es necesario
        // context.setState(new ConfirmedState());
    }
}

