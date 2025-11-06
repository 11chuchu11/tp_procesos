-- Create profiles table
CREATE TABLE IF NOT EXISTS profiles (
    profile_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    roles VARCHAR(255) NOT NULL,
    region VARCHAR(100) NOT NULL,
    tier VARCHAR(255),
    is_verify BOOLEAN DEFAULT FALSE
);


-- Create indexs
CREATE INDEX idx_profiles_is_verify ON profiles(is_verify);
CREATE INDEX idx_profiles_username ON profiles(username);
CREATE INDEX idx_profiles_profileId ON profiles(profile_id);
