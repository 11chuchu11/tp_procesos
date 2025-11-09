package com.example.demo.profile.repository;

import com.example.demo.enums.ProfileStatus;
import com.example.demo.profile.entity.Profile;
import com.example.demo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    List<Profile> findByStatus(ProfileStatus status);

    List<Profile> findByMainGameAndStatus(String mainGame, ProfileStatus status);

    @Query("SELECT p FROM Profile p WHERE p.status = :status AND p.mainGame = :mainGame " +
           "AND (:minTier IS NULL OR p.tier >= :minTier) " +
           "AND (:maxTier IS NULL OR p.tier <= :maxTier)")
    List<Profile> findAvailableProfilesForScrim(
        @Param("status") ProfileStatus status,
        @Param("mainGame") String mainGame,
        @Param("minTier") String minTier,
        @Param("maxTier") String maxTier
    );
}
