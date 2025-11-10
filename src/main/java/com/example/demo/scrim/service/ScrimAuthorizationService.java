package com.example.demo.scrim.service;

import com.example.demo.scrim.entity.Scrim;
import com.example.demo.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ScrimAuthorizationService {

    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new RuntimeException("User not authenticated");
        }
        return (User) authentication.getPrincipal();
    }

    public void validateScrimOwnership(Scrim scrim, User user) {
        if (!scrim.getCreatedBy().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Only the creator can perform this action");
        }
    }
}