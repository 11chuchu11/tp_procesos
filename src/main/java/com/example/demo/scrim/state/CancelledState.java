package com.example.demo.scrim.state;

import com.example.demo.scrim.entity.Scrim;

public class CancelledState implements ScrimState {
    
    @Override
    public String getStateName() {
        return "CANCELLED";
    }

    @Override
    public void handle(Scrim context) {
        // Lógica específica del estado Cancelled
        // Estado final, no hay transiciones
    }
}

