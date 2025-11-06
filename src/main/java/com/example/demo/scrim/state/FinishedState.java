package com.example.demo.scrim.state;

import com.example.demo.scrim.entity.Scrim;

public class FinishedState implements ScrimState {
    
    @Override
    public String getStateName() {
        return "FINISHED";
    }

    @Override
    public void handle(Scrim context) {
        // Lógica específica del estado Finished
        // Estado final, no hay transiciones
    }
}

