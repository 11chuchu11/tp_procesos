-- Create lobbies table first
CREATE TABLE IF NOT EXISTS lobbies (
    lobby_id BIGSERIAL PRIMARY KEY,
    format_type VARCHAR(10) NOT NULL
);
