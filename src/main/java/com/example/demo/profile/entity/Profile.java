package com.example.demo.profile.entity;

import com.example.demo.enums.ProfileStatus;
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

    @Column(name = "main_game", nullable = false, length = 255)
    private String mainGame;

    @Column(name = "tier", nullable = false, length = 255)
    private String tier;

    @Column(name = "region", nullable = false, length = 100)
    private String region;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProfileStatus status;

    public Profile() {
        this.status = ProfileStatus.AVAILABLE;
    }

    public Profile(User user, String mainGame, String tier, String region) {
        this.user = user;
        this.mainGame = mainGame;
        this.tier = tier;
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

    public String getMainGame() {
        return mainGame;
    }

    public void setMainGame(String mainGame) {
        this.mainGame = mainGame;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public ProfileStatus getStatus() {
        return status;
    }

    public void setStatus(ProfileStatus status) {
        this.status = status;
    }
}
