-- League of Legends profiles
INSERT INTO profiles (user_id, main_game_id, main_tier_id, region_id, status) VALUES
((SELECT user_id FROM users WHERE username='ShatteredBlade'), 
 (SELECT game_id FROM games WHERE game_name='League of Legends'),
 (SELECT tier_id FROM tiers WHERE tier_name='Diamond' AND game_id=(SELECT game_id FROM games WHERE game_name='League of Legends')),
 (SELECT region_id FROM regions WHERE region_name='LAS'), 
 'AVAILABLE'),
 
((SELECT user_id FROM users WHERE username='NebulaCaster'), 
 (SELECT game_id FROM games WHERE game_name='League of Legends'),
 (SELECT tier_id FROM tiers WHERE tier_name='Diamond' AND game_id=(SELECT game_id FROM games WHERE game_name='League of Legends')),
 (SELECT region_id FROM regions WHERE region_name='LAS'), 
 'AVAILABLE'),
 
((SELECT user_id FROM users WHERE username='CrimsonMarksman'), 
 (SELECT game_id FROM games WHERE game_name='League of Legends'),
 (SELECT tier_id FROM tiers WHERE tier_name='Diamond' AND game_id=(SELECT game_id FROM games WHERE game_name='League of Legends')),
 (SELECT region_id FROM regions WHERE region_name='LAS'), 
 'AVAILABLE'),
 
((SELECT user_id FROM users WHERE username='RustyLantern'), 
 (SELECT game_id FROM games WHERE game_name='League of Legends'),
 (SELECT tier_id FROM tiers WHERE tier_name='Iron' AND game_id=(SELECT game_id FROM games WHERE game_name='League of Legends')),
 (SELECT region_id FROM regions WHERE region_name='LAS'), 
 'AVAILABLE');

-- Valorant profiles
INSERT INTO profiles (user_id, main_game_id, main_tier_id, region_id, status) VALUES
((SELECT user_id FROM users WHERE username='VandalVirtuoso'), 
 (SELECT game_id FROM games WHERE game_name='Valorant'),
 (SELECT tier_id FROM tiers WHERE tier_name='Immortal' AND game_id=(SELECT game_id FROM games WHERE game_name='Valorant')),
 (SELECT region_id FROM regions WHERE region_name='LAS'), 
 'AVAILABLE'),
 
((SELECT user_id FROM users WHERE username='SilentCrosshair'), 
 (SELECT game_id FROM games WHERE game_name='Valorant'),
 (SELECT tier_id FROM tiers WHERE tier_name='Immortal' AND game_id=(SELECT game_id FROM games WHERE game_name='Valorant')),
 (SELECT region_id FROM regions WHERE region_name='LAS'), 
 'AVAILABLE');

-- Dota 2 profiles
INSERT INTO profiles (user_id, main_game_id, main_tier_id, region_id, status) VALUES
((SELECT user_id FROM users WHERE username='ArcaneHarvester'), 
 (SELECT game_id FROM games WHERE game_name='Dota 2'),
 (SELECT tier_id FROM tiers WHERE tier_name='Archon' AND game_id=(SELECT game_id FROM games WHERE game_name='Dota 2')),
 (SELECT region_id FROM regions WHERE region_name='LAS'), 
 'AVAILABLE'),
 
((SELECT user_id FROM users WHERE username='SoulbreakerPrime'), 
 (SELECT game_id FROM games WHERE game_name='Dota 2'),
 (SELECT tier_id FROM tiers WHERE tier_name='Ancient' AND game_id=(SELECT game_id FROM games WHERE game_name='Dota 2')),
 (SELECT region_id FROM regions WHERE region_name='LAS'), 
 'AVAILABLE'),
 
((SELECT user_id FROM users WHERE username='DivineTempest'), 
 (SELECT game_id FROM games WHERE game_name='Dota 2'),
 (SELECT tier_id FROM tiers WHERE tier_name='Divine' AND game_id=(SELECT game_id FROM games WHERE game_name='Dota 2')),
 (SELECT region_id FROM regions WHERE region_name='LAS'), 
 'AVAILABLE');

