package com.example.demo.tier.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tiers")
public class Tier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tier_id")
    private Long tierId;

    @Column(name = "tier_name", nullable = false, unique = true, length = 100)
    private String tierName;

    public Tier() {
    }

    public Tier(String tierName) {
        this.tierName = tierName;
    }

    public Long getTierId() {
        return tierId;
    }

    public void setTierId(Long tierId) {
        this.tierId = tierId;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

}
