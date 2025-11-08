CREATE TABLE IF NOT EXISTS teams (
    team_id BIGSERIAL PRIMARY KEY,
    lobby_id BIGINT NOT NULL,
    format_type VARCHAR(10) NOT NULL,
    CONSTRAINT fk_teams_lobby FOREIGN KEY (lobby_id) REFERENCES lobbies(lobby_id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_teams_lobby_id ON teams(lobby_id);