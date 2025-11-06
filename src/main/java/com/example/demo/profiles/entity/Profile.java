package com.example.demo.profiles.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "main_game", nullable = false, length = 255)
    private String mainGame;

    @Column(name = "tier", nullable = false, length = 255)
    private String tier;

    @Column(name = "roles", nullable = false, length = 255)
    private String roles;

    @Column(name = "region", nullable = false, length = 100)
    private String region;

    @Column(name = "is_verify", nullable = false)
    private Boolean isVerify = false;

    public Profile() {
    }

    public Profile(String username, String main_game, String tier, String roles, String region) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isVerify = false;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Boolean getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Boolean getIsVerify() {
        return isVerify;
    }

    public void setIsVerify(Boolean isVerify) {
        this.isVerify = isVerify;
    }
}
