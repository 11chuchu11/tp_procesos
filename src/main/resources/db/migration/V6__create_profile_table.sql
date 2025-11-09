CREATE TABLE IF NOT EXISTS profiles (
    profile_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    main_game_id BIGINT NOT NULL,
    main_tier_id BIGINT NOT NULL,
    region_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE',
    CONSTRAINT fk_profiles_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_profiles_game FOREIGN KEY (main_game_id) REFERENCES games(game_id) ON DELETE RESTRICT,
    CONSTRAINT fk_profiles_tier FOREIGN KEY (main_tier_id) REFERENCES tiers(tier_id) ON DELETE RESTRICT,
    CONSTRAINT fk_profiles_region FOREIGN KEY (region_id) REFERENCES regions(region_id) ON DELETE RESTRICT
);

CREATE INDEX idx_profiles_user_id ON profiles(user_id);
CREATE INDEX idx_profiles_profile_id ON profiles(profile_id);
CREATE INDEX idx_profiles_status ON profiles(status);
CREATE INDEX idx_profiles_main_game_id ON profiles(main_game_id);
CREATE INDEX idx_profiles_main_tier_id ON profiles(main_tier_id);
CREATE INDEX idx_profiles_region_id ON profiles(region_id);

