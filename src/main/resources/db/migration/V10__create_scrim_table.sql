CREATE TABLE IF NOT EXISTS scrims (
    scrim_id BIGSERIAL PRIMARY KEY,
    status VARCHAR(50) NOT NULL,
    format_type VARCHAR(10),
    lobby_id BIGINT,
    game_id BIGINT NOT NULL,
    created_by_user_id BIGINT NOT NULL,
    min_tier_id BIGINT,
    max_tier_id BIGINT,
    region_id BIGINT,
    scheduled_time TIMESTAMP NOT NULL,
    CONSTRAINT fk_scrims_lobby FOREIGN KEY (lobby_id) REFERENCES lobbies(lobby_id) ON DELETE SET NULL,
    CONSTRAINT fk_scrims_game FOREIGN KEY (game_id) REFERENCES games(game_id) ON DELETE RESTRICT,
    CONSTRAINT fk_scrims_created_by_user FOREIGN KEY (created_by_user_id) REFERENCES users(user_id) ON DELETE RESTRICT,
    CONSTRAINT fk_scrims_min_tier FOREIGN KEY (min_tier_id) REFERENCES tiers(tier_id) ON DELETE RESTRICT,
    CONSTRAINT fk_scrims_max_tier FOREIGN KEY (max_tier_id) REFERENCES tiers(tier_id) ON DELETE RESTRICT,
    CONSTRAINT fk_scrims_region FOREIGN KEY (region_id) REFERENCES regions(region_id) ON DELETE RESTRICT
);

CREATE INDEX idx_scrims_status ON scrims(status);
CREATE INDEX idx_scrims_lobby_id ON scrims(lobby_id);
CREATE INDEX idx_scrims_game_id ON scrims(game_id);
CREATE INDEX idx_scrims_created_by_user_id ON scrims(created_by_user_id);
CREATE INDEX idx_scrims_min_tier_id ON scrims(min_tier_id);
CREATE INDEX idx_scrims_max_tier_id ON scrims(max_tier_id);
CREATE INDEX idx_scrims_region_id ON scrims(region_id);

