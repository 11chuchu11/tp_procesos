package com.example.demo.scrim.state;

import com.example.demo.scrim.entity.Scrim;

public interface ScrimState {
    String getStateName();
    void apply(Scrim context) throws IllegalStateException;
    void cancel(Scrim context) throws IllegalStateException;
    void finish(Scrim context) throws IllegalStateException;
    
    default void unsupportedOperation(String operation) {
        throw new IllegalStateException(
            "Cannot " + operation + " a scrim in " + getStateName() + " state"
        );
    }
}

