-- Create tiers table
CREATE TABLE IF NOT EXISTS tiers (
    tier_id BIGSERIAL PRIMARY KEY,
    game_id BIGINT NOT NULL REFERENCES games(game_id),
    tier_name VARCHAR(100) NOT NULL UNIQUE,
    rank INTEGER NOT NULL DEFAULT 0
);

-- Create indexes
CREATE INDEX idx_tiers_tier_id ON tiers(tier_id);
CREATE INDEX idx_tiers_tier_name ON tiers(tier_name);


-- Inserts
INSERT INTO tiers (game_id, tier_name, rank) VALUES
                                                 (1, 'LoL Iron', 1),
                                                 (1, 'LoL Bronze', 2),
                                                 (1, 'LoL Silver', 3),
                                                 (1, 'LoL Gold', 4),
                                                 (1, 'LoL Platinum', 5),
                                                 (1, 'LoL Diamond', 6),
                                                 (1, 'LoL Master', 7),
                                                 (1, 'LoL Grandmaster', 8),
                                                 (1, 'LoL Challenger', 9);


INSERT INTO tiers (game_id, tier_name, rank) VALUES
                                                 (2, 'Valorant Iron', 1),
                                                 (2, 'Valorant Bronze', 2),
                                                 (2, 'Valorant Silver', 3),
                                                 (2, 'Valorant Gold', 4),
                                                 (2, 'Valorant Platinum', 5),
                                                 (2, 'Valorant Diamond', 6),
                                                 (2, 'Valorant Ascendant', 7),
                                                 (2, 'Valorant Immortal', 8),
                                                 (2, 'Valorant Radiant', 9);


INSERT INTO tiers (game_id, tier_name, rank) VALUES
                                                 (3, 'Dota Herald', 1),
                                                 (3, 'Dota Guardian', 2),
                                                 (3, 'Dota Crusader', 3),
                                                 (3, 'Dota Archon', 4),
                                                 (3, 'Dota Legend', 5),
                                                 (3, 'Dota Ancient', 6),
                                                 (3, 'Dota Divine', 7),
                                                 (3, 'Dota Immortal', 8);
