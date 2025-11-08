package com.example.demo.tier.entity;

import com.example.demo.game.entity.Game;
import jakarta.persistence.*;

@Entity
@Table(name = "tiers")
public class Tier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tier_id")
    private Long tierId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(name = "tier_name", nullable = false, unique = true, length = 100)
    private String tierName;

    @Column(name = "rank", nullable = false)
    private Integer rank;

    public Tier() {
    }

    public Tier(Game game, String tierName, Integer rank) {
        this.game = game;
        this.tierName = tierName;
        this.rank = rank;
    }

    public Long getTierId() {
        return tierId;
    }

    public void setTierId(Long tierId) {
        this.tierId = tierId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    /**
     * Compares this tier with another tier based on rank
     * @return positive if this tier is higher, negative if lower, 0 if equal
     */
    public int compareTo(Tier other) {
        if (other == null || other.getRank() == null) {
            return 1;
        }
        return this.rank.compareTo(other.getRank());
    }

    /**
     * Checks if this tier is higher than the given tier
     */
    public boolean isHigherThan(Tier other) {
        return compareTo(other) > 0;
    }

    /**
     * Checks if this tier is lower than the given tier
     */
    public boolean isLowerThan(Tier other) {
        return compareTo(other) < 0;
    }

    /**
     * Checks if this tier is in the range [minTier, maxTier] inclusive
     */
    public boolean isInRange(Tier minTier, Tier maxTier) {
        if (minTier == null && maxTier == null) {
            return true;
        }
        if (minTier == null) {
            return this.rank <= maxTier.getRank();
        }
        if (maxTier == null) {
            return this.rank >= minTier.getRank();
        }
        return this.rank >= minTier.getRank() && this.rank <= maxTier.getRank();
    }
}
