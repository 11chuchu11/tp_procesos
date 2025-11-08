CREATE TABLE IF NOT EXISTS scrims (
    scrim_id BIGSERIAL PRIMARY KEY,
    status VARCHAR(50) NOT NULL,
    format_type VARCHAR(10),
    lobby_id BIGINT,
    game_id BIGINT NOT NULL,
    CONSTRAINT fk_scrims_lobby FOREIGN KEY (lobby_id) REFERENCES lobbies(lobby_id) ON DELETE SET NULL,
    CONSTRAINT fk_scrims_game FOREIGN KEY (game_id) REFERENCES games(game_id) ON DELETE RESTRICT
);

-- Create indexes
CREATE INDEX idx_scrims_status ON scrims(status);
CREATE INDEX idx_scrims_lobby_id ON scrims(lobby_id);
CREATE INDEX idx_scrims_game_id ON scrims(game_id);