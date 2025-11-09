CREATE TABLE IF NOT EXISTS profiles (
    profile_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    main_game VARCHAR(255) NOT NULL,
    tier VARCHAR(255) NOT NULL,
    region VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE',
    CONSTRAINT fk_profiles_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE INDEX idx_profiles_user_id ON profiles(user_id);
CREATE INDEX idx_profiles_profile_id ON profiles(profile_id);
CREATE INDEX idx_profiles_status ON profiles(status);


-- Inserts
-- LOL
INSERT INTO profiles (user_id, main_game, tier, region, status) VALUES
                                                                    ((SELECT user_id FROM users WHERE username='ShatteredBlade'),   'League of Legends', 'Diamond', 'LAS', 'AVAILABLE'),
                                                                    ((SELECT user_id FROM users WHERE username='NebulaCaster'),     'League of Legends', 'Diamond', 'LAS', 'AVAILABLE'),
                                                                    ((SELECT user_id FROM users WHERE username='CrimsonMarksman'),  'League of Legends', 'Diamond', 'LAS', 'AVAILABLE'),
                                                                    ((SELECT user_id FROM users WHERE username='RustyLantern'),     'League of Legends', 'Iron',    'LAS', 'AVAILABLE');

-- VALO
INSERT INTO profiles (user_id, main_game, tier, region, status) VALUES
                                                                    ((SELECT user_id FROM users WHERE username='VandalVirtuoso'),   'Valorant', 'Immortal', 'LATAM-S', 'AVAILABLE'),
                                                                    ((SELECT user_id FROM users WHERE username='SilentCrosshair'),  'Valorant', 'Immortal', 'LATAM-S', 'AVAILABLE');

-- DOTA
INSERT INTO profiles (user_id, main_game, tier, region, status) VALUES
                                                                    ((SELECT user_id FROM users WHERE username='ArcaneHarvester'),  'Dota 2', 'Archon',  'SA', 'AVAILABLE'),
                                                                    ((SELECT user_id FROM users WHERE username='SoulbreakerPrime'), 'Dota 2', 'Ancient', 'SA', 'AVAILABLE'),
                                                                    ((SELECT user_id FROM users WHERE username='DivineTempest'),    'Dota 2', 'Divine',  'SA', 'AVAILABLE');

