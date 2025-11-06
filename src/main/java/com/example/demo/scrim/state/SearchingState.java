package com.example.demo.scrim.state;

import com.example.demo.scrim.entity.Scrim;

public class SearchingState implements ScrimState {
    
    @Override
    public String getStateName() {
        return "SEARCHING";
    }

    @Override
    public void handle(Scrim context) {
        // Lógica específica del estado Searching
        // Transición al siguiente estado si es necesario
        // context.setState(new LobbyReadyState());
    }
}

