-- Repeatable seed: Zimbabwe cities, suburbs, and safe meetup points.
-- Re-runs whenever this file's checksum changes.

-- ============================================================
-- CITIES & SUBURBS
-- ============================================================

INSERT INTO loc_cities (name, province, latitude, longitude) VALUES
-- Metropolitan cities
('Harare',          'Harare Metropolitan',      -17.8292,  31.0522),
('Bulawayo',        'Bulawayo Metropolitan',    -20.1325,  28.6265),

-- Manicaland
('Mutare',          'Manicaland',               -18.9706,  32.6709),
('Chipinge',        'Manicaland',               -20.1907,  32.6234),

-- Mashonaland Central
('Bindura',         'Mashonaland Central',      -17.3033,  31.3315),
('Mvurwi',          'Mashonaland Central',      -17.0333,  30.8500),

-- Mashonaland East
('Marondera',       'Mashonaland East',         -18.1850,  31.5519),
('Ruwa',            'Mashonaland East',         -17.8883,  31.2455),
('Chitungwiza',     'Mashonaland East',         -18.0127,  31.0716),

-- Mashonaland West
('Chinhoyi',        'Mashonaland West',         -17.3610,  30.1975),
('Kadoma',          'Mashonaland West',         -18.3404,  29.9086),
('Chegutu',         'Mashonaland West',         -18.1435,  30.1441),
('Kariba',          'Mashonaland West',         -16.5226,  28.7946),
('Norton',          'Mashonaland West',         -17.8763,  30.7003),

-- Masvingo
('Masvingo',        'Masvingo',                 -20.0624,  30.8277),
('Zvishavane',      'Masvingo',                 -20.3395,  29.9998),
('Mberengwa',       'Masvingo',                 -20.4667,  29.7333),

-- Matabeleland North
('Victoria Falls',  'Matabeleland North',       -17.9316,  25.8306),
('Hwange',          'Matabeleland North',       -18.3671,  26.5007),

-- Matabeleland South
('Beitbridge',      'Matabeleland South',       -22.2218,  30.0034),

-- Midlands
('Gweru',           'Midlands',                 -19.4536,  29.8163),
('Kwekwe',          'Midlands',                 -18.9282,  29.8149),
('Redcliff',        'Midlands',                 -19.0328,  29.7850),
('Shurugwi',        'Midlands',                 -19.6667,  30.0000),
('Gokwe',           'Midlands',                 -18.2167,  28.9333),

-- Harare suburbs
('Avondale',        'Harare Metropolitan',      -17.7957,  31.0293),
('Borrowdale',      'Harare Metropolitan',      -17.7371,  31.0909),
('Greendale',       'Harare Metropolitan',      -17.8167,  31.0985),
('Highlands',       'Harare Metropolitan',      -17.8007,  31.0633),
('Mbare',           'Harare Metropolitan',      -17.8581,  31.0388),
('Budiriro',        'Harare Metropolitan',      -17.9016,  30.9764),
('Mufakose',        'Harare Metropolitan',      -17.8806,  30.9719),
('Glen Norah',      'Harare Metropolitan',      -17.8869,  31.0015),
('Epworth',         'Harare Metropolitan',      -17.9001,  31.1783),
('Hatfield',        'Harare Metropolitan',      -17.8674,  31.1046),
('Waterfalls',      'Harare Metropolitan',      -17.8996,  31.0134),

-- Bulawayo suburbs
('Suburbs',         'Bulawayo Metropolitan',    -20.1500,  28.5833),
('Pumula',          'Bulawayo Metropolitan',    -20.2000,  28.5667),
('Nkulumane',       'Bulawayo Metropolitan',    -20.1333,  28.5833),
('Luveve',          'Bulawayo Metropolitan',    -20.1667,  28.5500),
('Nketa',           'Bulawayo Metropolitan',    -20.1792,  28.5531)

ON CONFLICT DO NOTHING;

-- ============================================================
-- SAFE MEETUP POINTS
-- ============================================================

INSERT INTO loc_safe_meetup_points (city_id, name, description, address, latitude, longitude)
SELECT c.id, mp.name, mp.description, mp.address, mp.latitude, mp.longitude
FROM (VALUES
    -- Harare meetup points
    ('Harare', 'Sam Levy''s Village (Main Entrance)',   'Popular mall in Borrowdale, well-lit and busy',       'Sam Levy''s Village, Borrowdale Rd, Borrowdale',     -17.7371,  31.0909),
    ('Harare', 'Eastgate Shopping Centre (Ground Floor)','Central CBD mall with security and high foot traffic', 'Eastgate Centre, Robert Mugabe Rd, Harare CBD',      -17.8295,  31.0528),
    ('Harare', 'Westgate Shopping Centre (Entrance)',   'Safe suburban mall with parking and security',        'Westgate Shopping Centre, Westgate',                 -17.8004,  30.9842),
    ('Harare', 'Avondale Shopping Centre',              'Busy suburban shopping area',                        'Avondale Shopping Centre, King George Rd, Avondale', -17.7957,  31.0293),
    ('Harare', 'Harare Central Police Station',         'Official police station — maximum safety',           'Corner Inez Terrace & Rhodesville Ave, Harare CBD',  -17.8313,  31.0486),

    -- Bulawayo meetup points
    ('Bulawayo', 'Bulawayo Centre (Main Entrance)',     'Large central mall in the heart of Bulawayo',        'Bulawayo Centre, L. Takawira / 9th Ave, Bulawayo',   -20.1511,  28.5843),
    ('Bulawayo', 'Ascot Shopping Centre',               'Well-trafficked suburban mall with good security',   'Ascot Shopping Centre, Ascot',                       -20.1650,  28.5700),
    ('Bulawayo', 'Bulawayo Central Police Station',     'Official police station — maximum safety',           'Corner Leopold Takawira Ave & 8th Ave, Bulawayo',    -20.1500,  28.5850)
) AS mp(city_name, name, description, address, latitude, longitude)
JOIN loc_cities c ON lower(c.name) = lower(mp.city_name)

ON CONFLICT DO NOTHING;
