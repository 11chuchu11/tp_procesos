package com.example.demo.tier.repository;

import com.example.demo.tier.entity.Tier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TierRepository extends JpaRepository<Tier, Long>, JpaSpecificationExecutor<Tier> {

    Optional<Tier> findByName(String tierName);

    boolean existsByName(String tierName);
}
