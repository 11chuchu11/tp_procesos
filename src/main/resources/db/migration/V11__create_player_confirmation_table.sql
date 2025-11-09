CREATE TABLE IF NOT EXISTS player_confirmations (
    confirmation_id BIGSERIAL PRIMARY KEY,
    scrim_id BIGINT NOT NULL,
    profile_id BIGINT NOT NULL,
    confirmed BOOLEAN NOT NULL DEFAULT FALSE,
    confirmed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_confirmation_scrim FOREIGN KEY (scrim_id) REFERENCES scrims(scrim_id) ON DELETE CASCADE,
    CONSTRAINT fk_confirmation_profile FOREIGN KEY (profile_id) REFERENCES profiles(profile_id) ON DELETE CASCADE,
    CONSTRAINT uk_scrim_profile UNIQUE (scrim_id, profile_id)
);

CREATE INDEX idx_confirmations_scrim_id ON player_confirmations(scrim_id);
CREATE INDEX idx_confirmations_profile_id ON player_confirmations(profile_id);

