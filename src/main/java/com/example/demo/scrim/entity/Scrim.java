package com.example.demo.scrim.entity;

import com.example.demo.enums.ScrimStatus;
import com.example.demo.format.strategy.Format;
import com.example.demo.scrim.factory.ScrimStateFactory;
import com.example.demo.scrim.state.ScrimState;
import jakarta.persistence.*;

@Entity
public class Scrim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scrimId;

    @Enumerated(EnumType.STRING)
    private ScrimStatus status;

    @Transient
    private ScrimState state;

    private Format format;



    @PostLoad
    public void onLoad() {
        this.state = ScrimStateFactory.fromStatus(this.status);
    }

    public void setState(ScrimState state) {
        this.state = state;
        this.status = ScrimStatus.valueOf(state.getStateName());
    }
}
