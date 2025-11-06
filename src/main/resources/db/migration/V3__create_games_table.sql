-- Create games table
CREATE TABLE IF NOT EXISTS games (
    game_id BIGSERIAL PRIMARY KEY,
    gameName VARCHAR(100) NOT NULL UNIQUE,
);


-- Create indexs
CREATE INDEX idx_games_gameName ON games(gameName);
CREATE INDEX idx_games_gameId ON games(game_id);
