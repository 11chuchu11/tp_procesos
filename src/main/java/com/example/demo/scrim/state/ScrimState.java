package com.example.demo.scrim.state;

import com.example.demo.scrim.entity.Scrim;

public interface ScrimState {
    String getStateName();
    void handle(Scrim context);
}

