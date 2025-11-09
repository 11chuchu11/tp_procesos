-- Create tiers table
CREATE TABLE IF NOT EXISTS tiers (
    tier_id BIGSERIAL PRIMARY KEY,
    game_id BIGINT NOT NULL REFERENCES games(game_id),
    tier_name VARCHAR(100) NOT NULL,
    rank INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT unique_tier_per_game UNIQUE (game_id, tier_name)
);

-- Create indexes
CREATE INDEX idx_tiers_tier_id ON tiers(tier_id);
CREATE INDEX idx_tiers_game_id ON tiers(game_id);
CREATE INDEX idx_tiers_tier_name ON tiers(tier_name);


-- Inserts
INSERT INTO tiers (game_id, tier_name, rank) VALUES
(1, 'Iron', 1),
(1, 'Bronze', 2),
(1, 'Silver', 3),
(1, 'Gold', 4),
(1, 'Platinum', 5),
(1, 'Diamond', 6),
(1, 'Master', 7),
(1, 'Grandmaster', 8),
(1, 'Challenger', 9);


INSERT INTO tiers (game_id, tier_name, rank) VALUES
(2, 'Iron', 1),
(2, 'Bronze', 2),
(2, 'Silver', 3),
(2, 'Gold', 4),
(2, 'Platinum', 5),
(2, 'Diamond', 6),
(2, 'Ascendant', 7),
(2, 'Immortal', 8),
(2, 'Radiant', 9);


INSERT INTO tiers (game_id, tier_name, rank) VALUES
(3, 'Herald', 1),
(3, 'Guardian', 2),
(3, 'Crusader', 3),
(3, 'Archon', 4),
(3, 'Legend', 5),
(3, 'Ancient', 6),
(3, 'Divine', 7),
(3, 'Immortal', 8);

