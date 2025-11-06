-- Create tiers table
CREATE TABLE IF NOT EXISTS tiers (
    tier_id BIGSERIAL PRIMARY KEY,
    game_id BIGINT NOT NULL REFERENCES games(game_id),
    tier_name VARCHAR(100) NOT NULL UNIQUE
);

-- Create indexes
CREATE INDEX idx_tiers_tier_id ON tiers(tier_id);
CREATE INDEX idx_tiers_tier_name ON tiers(tier_name);
