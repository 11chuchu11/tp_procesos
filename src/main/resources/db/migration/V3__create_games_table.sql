-- Create games table
CREATE TABLE IF NOT EXISTS games (
    game_id BIGSERIAL PRIMARY KEY,
    game_name VARCHAR(100) NOT NULL UNIQUE
);


-- Create indexes
CREATE INDEX idx_games_game_name ON games(game_name);
CREATE INDEX idx_games_game_id ON games(game_id);


-- Inserts
INSERT INTO games (game_name) VALUES
                                  ('League of Legends'),
                                  ('Valorant'),
                                  ('Dota 2');