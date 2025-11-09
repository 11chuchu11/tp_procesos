package com.example.demo.scrim.repository;

import com.example.demo.profile.entity.Profile;
import com.example.demo.scrim.entity.PlayerConfirmation;
import com.example.demo.scrim.entity.Scrim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerConfirmationRepository extends JpaRepository<PlayerConfirmation, Long> {

    Optional<PlayerConfirmation> findByScrimAndProfile(Scrim scrim, Profile profile);

    List<PlayerConfirmation> findByScrim(Scrim scrim);

    long countByScrim(Scrim scrim);

    long countByScrimAndConfirmed(Scrim scrim, Boolean confirmed);

    boolean existsByScrimAndProfile(Scrim scrim, Profile profile);
}

