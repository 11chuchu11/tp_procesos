 package com.example.demo.scrim.repository;

import com.example.demo.enums.ScrimStatus;
import com.example.demo.scrim.entity.Scrim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScrimRepository extends JpaRepository<Scrim, Long>, JpaSpecificationExecutor<Scrim> {

    List<Scrim> findByStatusIn(List<ScrimStatus> statuses);

    boolean existsByScrimIdAndStatus(Long scrimId, ScrimStatus status);
}

