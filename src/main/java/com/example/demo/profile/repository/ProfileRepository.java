package com.example.demo.profile.repository;

import com.example.demo.profile.entity.Profile;
import com.example.demo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>, JpaSpecificationExecutor<Profile> {

    Optional<Profile> findByUser(User user);

    Optional<Profile> findByUser_UserId(Long userId);

    List<Profile> findByRegion(String region);

    List<Profile> findByMainGame(String mainGame);

    List<Profile> findByTier(String tier);

    boolean existsByUser(User user);

    boolean existsByUser_UserId(Long userId);
}
