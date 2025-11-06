package com.example.demo.scrim.state;

import com.example.demo.scrim.entity.Scrim;

public class ConfirmedState implements ScrimState {
    
    @Override
    public String getStateName() {
        return "CONFIRMED";
    }

    @Override
    public void handle(Scrim context) {
        // Lógica específica del estado Confirmed
        // Transición al siguiente estado si es necesario
        // context.setState(new InGameState());
    }
}

