CREATE TABLE IF NOT EXISTS regions (
    region_id BIGSERIAL PRIMARY KEY,
    region_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE INDEX idx_regions_region_id ON regions(region_id);
CREATE INDEX idx_regions_region_name ON regions(region_name);

INSERT INTO regions (region_name) VALUES 
('EU'),
('NA'),
('LAS');

