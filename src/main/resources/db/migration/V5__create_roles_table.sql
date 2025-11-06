-- Create roles table
CREATE TABLE IF NOT EXISTS roles (
    role_id BIGSERIAL PRIMARY KEY,
    game_id BIGINT NOT NULL REFERENCES games(game_id),
    role_name VARCHAR(100) NOT NULL UNIQUE
);

-- Create indexes
CREATE INDEX idx_roles_role_id ON roles(role_id);
CREATE INDEX idx_roles_role_name ON roles(role_name);
