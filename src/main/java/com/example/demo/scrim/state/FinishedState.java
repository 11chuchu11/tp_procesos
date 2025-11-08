package com.example.demo.scrim.state;

import com.example.demo.scrim.entity.Scrim;

public class FinishedState implements ScrimState {
    
    @Override
    public String getStateName() {
        return "FINISHED";
    }

    @Override
    public void apply(Scrim context) {
        unsupportedOperation("apply");
    }

    @Override
    public void cancel(Scrim context) {
        unsupportedOperation("cancel");
    }

    @Override
    public void finish(Scrim context) {
        unsupportedOperation("finish");
    }
}

