-- Create roles table
CREATE TABLE IF NOT EXISTS roles (
    role_id BIGSERIAL PRIMARY KEY,
    game_id BIGINT NOT NULL REFERENCES games(game_id),
    role_name VARCHAR(100) NOT NULL,
    CONSTRAINT unique_role_per_game UNIQUE (game_id, role_name)
);

-- Create indexes
CREATE INDEX idx_roles_role_id ON roles(role_id);
CREATE INDEX idx_roles_game_id ON roles(game_id);
CREATE INDEX idx_roles_role_name ON roles(role_name);


-- Inserts
INSERT INTO roles (game_id, role_name) VALUES
(1, 'Top'),
(1, 'Jungle'),
(1, 'Mid'),
(1, 'ADC'),
(1, 'Support');

INSERT INTO roles (game_id, role_name) VALUES
(2, 'Duelist'),
(2, 'Controller'),
(2, 'Sentinel'),
(2, 'Initiator');

INSERT INTO roles (game_id, role_name) VALUES
(3, 'Carry'),
(3, 'Mid'),
(3, 'Offlane'),
(3, 'Soft Support'),
(3, 'Hard Support');
