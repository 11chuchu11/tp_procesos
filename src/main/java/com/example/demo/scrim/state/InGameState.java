package com.example.demo.scrim.state;

import com.example.demo.scrim.entity.Scrim;

public class InGameState implements ScrimState {
    
    @Override
    public String getStateName() {
        return "INGAME";
    }

    @Override
    public void handle(Scrim context) {
        // Lógica específica del estado InGame
        // Transición al siguiente estado si es necesario
        // context.setState(new FinishedState());
    }
}

