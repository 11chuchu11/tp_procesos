package com.example.demo.profile.entity;

import com.example.demo.enums.ProfileStatus;
import com.example.demo.game.entity.Game;
import com.example.demo.tier.entity.Tier;
import com.example.demo.user.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_game_id", nullable = false)
    private Game mainGame;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_tier_id", nullable = false)
    private Tier mainTier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private com.example.demo.region.entity.Region region;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProfileStatus status;

    public Profile() {
        this.status = ProfileStatus.AVAILABLE;
    }

    public Profile(User user, Game mainGame, Tier mainTier, com.example.demo.region.entity.Region region) {
        this.user = user;
        this.mainGame = mainGame;
        this.mainTier = mainTier;
        this.region = region;
        this.status = ProfileStatus.AVAILABLE;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Game getMainGame() {
        return mainGame;
    }

    public void setMainGame(Game mainGame) {
        this.mainGame = mainGame;
    }

    public Tier getMainTier() {
        return mainTier;
    }

    public void setMainTier(Tier mainTier) {
        this.mainTier = mainTier;
    }

    public com.example.demo.region.entity.Region getRegion() {
        return region;
    }

    public void setRegion(com.example.demo.region.entity.Region region) {
        this.region = region;
    }

    public ProfileStatus getStatus() {
        return status;
    }

    public void setStatus(ProfileStatus status) {
        this.status = status;
    }
}
