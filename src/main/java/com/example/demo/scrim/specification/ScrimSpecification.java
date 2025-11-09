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

    public static Specification<Scrim> hasRegion(String region) {
        return (root, query, criteriaBuilder) -> {
            if (region == null || region.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("region"), region);
        };
    }

    public static Specification<Scrim> hasMinTier(String minTier) {
        return (root, query, criteriaBuilder) -> {
            if (minTier == null || minTier.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("minTier"), minTier);
        };
    }

    public static Specification<Scrim> hasMaxTier(String maxTier) {
        return (root, query, criteriaBuilder) -> {
            if (maxTier == null || maxTier.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("maxTier"), maxTier);
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

