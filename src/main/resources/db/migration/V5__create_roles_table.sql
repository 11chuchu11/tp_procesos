-- Create roles table
CREATE TABLE IF NOT EXISTS roles (
    role_id BIGSERIAL PRIMARY KEY,
    game_id BIGINT NOT NULL REFERENCES games(game_id),
    role_name VARCHAR(100) NOT NULL UNIQUE,
);


-- Create indexs
CREATE INDEX idx_roles_roleId ON roles(role_id);
CREATE INDEX idx_roles_roleName ON roles(role_name);
