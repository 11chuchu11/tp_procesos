package com.example.demo.scrim.specification;

import com.example.demo.enums.ScrimStatus;
import com.example.demo.scrim.entity.Scrim;
import com.example.demo.game.entity.Game;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class ScrimSpecification {

    public static Specification<Scrim> hasGameId(Long gameId) {
        return (root, query, criteriaBuilder) -> {
            if (gameId == null) {
                return criteriaBuilder.conjunction();
            }
            Join<Scrim, Game> gameJoin = root.join("game");
            return criteriaBuilder.equal(gameJoin.get("gameId"), gameId);
        };
    }

    public static Specification<Scrim> hasFormatType(String formatType) {
        return (root, query, criteriaBuilder) -> {
            if (formatType == null || formatType.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("formatType"), formatType);
        };
    }

    public static Specification<Scrim> hasRegionId(Long regionId) {
        return (root, query, criteriaBuilder) -> {
            if (regionId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("region").get("regionId"), regionId);
        };
    }

    public static Specification<Scrim> hasMinTier(Long minTierId) {
        return (root, query, criteriaBuilder) -> {
            if (minTierId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("minTier").get("tierId"), minTierId);
        };
    }

    public static Specification<Scrim> hasMaxTier(Long maxTierId) {
        return (root, query, criteriaBuilder) -> {
            if (maxTierId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("maxTier").get("tierId"), maxTierId);
        };
    }

    public static Specification<Scrim> hasStatus(ScrimStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }
}

