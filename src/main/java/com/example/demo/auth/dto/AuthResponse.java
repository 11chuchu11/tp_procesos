package com.example.demo.auth.dto;

public class AuthResponse {

    private Long userId;
    private String username;
    private String email;
    private Boolean isVerify;
    private String token;

    public AuthResponse() {
    }

    public AuthResponse(Long userId, String username, String email, Boolean isVerify, String token) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.isVerify = isVerify;
        this.token = token;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsVerify() {
        return isVerify;
    }

    public void setIsVerify(Boolean isVerify) {
        this.isVerify = isVerify;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
