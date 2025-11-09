package com.example.demo.profile.repository;

import com.example.demo.enums.ProfileStatus;
import com.example.demo.game.entity.Game;
import com.example.demo.profile.entity.Profile;
import com.example.demo.region.entity.Region;
import com.example.demo.tier.entity.Tier;
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

    List<Profile> findByRegion(Region region);
    
    List<Profile> findByRegion_RegionId(Long regionId);

    List<Profile> findByMainGame(Game mainGame);
    
    List<Profile> findByMainGame_GameId(Long gameId);

    List<Profile> findByMainTier(Tier tier);
    
    List<Profile> findByMainTier_TierId(Long tierId);

    boolean existsByUser(User user);

    boolean existsByUser_UserId(Long userId);

    List<Profile> findByStatus(ProfileStatus status);

    List<Profile> findByMainGameAndStatus(Game mainGame, ProfileStatus status);
    
    List<Profile> findByMainGame_GameIdAndStatus(Long gameId, ProfileStatus status);

    @Query("SELECT p FROM Profile p WHERE p.status = :status AND p.mainGame.gameId = :gameId " +
           "AND (p.mainTier.rank >= :minTierRank) " +
           "AND (p.mainTier.rank <= :maxTierRank)")
    List<Profile> findAvailableProfilesForScrim(
        @Param("status") ProfileStatus status,
        @Param("gameId") Long gameId,
        @Param("minTierRank") Integer minTierRank,
        @Param("maxTierRank") Integer maxTierRank
    );
}
