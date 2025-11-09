-- Create user table
CREATE TABLE IF NOT EXISTS users (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    is_verify BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);

-- Inserts
INSERT INTO users (username, email, password, is_verify) VALUES
-- LOL
('ShatteredBlade',      'shattered.blade@hotmail.com',     'Password123!', TRUE),
('NebulaCaster',        'nebula.caster@hotmail.com',       'Password123!', TRUE),
('CrimsonMarksman',     'crimson.marksman@gmail.com',    'Password123!', TRUE),
('RustyLantern',        'rusty.lantern@gmail.com',       'Password123!', TRUE),
-- VALO

('VandalVirtuoso',      'vandal.virtuoso@hotmail.com',     'Password123!', TRUE),
('SilentCrosshair',     'silent.crosshair@gmail.com',    'Password123!', TRUE),

-- DOTA
('ArcaneHarvester',     'arcane.harvester@hotmail.com',   'Password123!', true),
('SoulbreakerPrime',    'soulbreaker.prime@gmail.com',  'Password123!', TRUE),
('DivineTempest',       'divine.tempest@gmail.com',     'Password123!', TRUE);