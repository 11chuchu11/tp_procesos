CREATE TABLE IF NOT EXISTS team_profiles (
    team_id BIGINT NOT NULL,
    profile_id BIGINT NOT NULL,
    PRIMARY KEY (team_id, profile_id),
    CONSTRAINT fk_team_profiles_team FOREIGN KEY (team_id) REFERENCES teams(team_id) ON DELETE CASCADE,
    CONSTRAINT fk_team_profiles_profile FOREIGN KEY (profile_id) REFERENCES profiles(profile_id) ON DELETE CASCADE
);

CREATE INDEX idx_team_profiles_team_id ON team_profiles(team_id);
CREATE INDEX idx_team_profiles_profile_id ON team_profiles(profile_id);