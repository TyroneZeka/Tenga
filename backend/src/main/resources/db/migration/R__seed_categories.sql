-- Repeatable migration: seed reference data for categories
-- Re-runs whenever the checksum changes.

INSERT INTO lst_categories (id, name, slug, sort_order, active, created_at, updated_at, version)
VALUES
    ('00000000-0000-0000-0001-000000000001', 'Electronics',       'electronics',       1, TRUE, now(), now(), 0),
    ('00000000-0000-0000-0001-000000000002', 'Vehicles',          'vehicles',          2, TRUE, now(), now(), 0),
    ('00000000-0000-0000-0001-000000000003', 'Furniture',         'furniture',         3, TRUE, now(), now(), 0),
    ('00000000-0000-0000-0001-000000000004', 'Clothing & Fashion','clothing-fashion',  4, TRUE, now(), now(), 0),
    ('00000000-0000-0000-0001-000000000005', 'Home & Garden',     'home-garden',       5, TRUE, now(), now(), 0),
    ('00000000-0000-0000-0001-000000000006', 'Sports & Leisure',  'sports-leisure',    6, TRUE, now(), now(), 0),
    ('00000000-0000-0000-0001-000000000007', 'Kids & Baby',       'kids-baby',         7, TRUE, now(), now(), 0),
    ('00000000-0000-0000-0001-000000000008', 'Books & Education', 'books-education',   8, TRUE, now(), now(), 0),
    ('00000000-0000-0000-0001-000000000009', 'Agriculture',       'agriculture',       9, TRUE, now(), now(), 0),
    ('00000000-0000-0000-0001-000000000010', 'Services',          'services',         10, TRUE, now(), now(), 0),
    ('00000000-0000-0000-0001-000000000011', 'Other',             'other',            99, TRUE, now(), now(), 0)
ON CONFLICT (id) DO NOTHING;

-- Sub-categories for Electronics
INSERT INTO lst_categories (id, name, slug, parent_id, sort_order, active, created_at, updated_at, version)
VALUES
    ('00000000-0000-0000-0002-000000000001', 'Phones & Tablets',  'phones-tablets',  '00000000-0000-0000-0001-000000000001', 1, TRUE, now(), now(), 0),
    ('00000000-0000-0000-0002-000000000002', 'Computers',         'computers',       '00000000-0000-0000-0001-000000000001', 2, TRUE, now(), now(), 0),
    ('00000000-0000-0000-0002-000000000003', 'TVs & Audio',       'tvs-audio',       '00000000-0000-0000-0001-000000000001', 3, TRUE, now(), now(), 0),
    ('00000000-0000-0000-0002-000000000004', 'Cameras',           'cameras',         '00000000-0000-0000-0001-000000000001', 4, TRUE, now(), now(), 0)
ON CONFLICT (id) DO NOTHING;

-- Sub-categories for Vehicles
INSERT INTO lst_categories (id, name, slug, parent_id, sort_order, active, created_at, updated_at, version)
VALUES
    ('00000000-0000-0000-0002-000000000010', 'Cars',              'cars',            '00000000-0000-0000-0001-000000000002', 1, TRUE, now(), now(), 0),
    ('00000000-0000-0000-0002-000000000011', 'Motorcycles',       'motorcycles',     '00000000-0000-0000-0001-000000000002', 2, TRUE, now(), now(), 0),
    ('00000000-0000-0000-0002-000000000012', 'Trucks & Combis',   'trucks-combis',   '00000000-0000-0000-0001-000000000002', 3, TRUE, now(), now(), 0),
    ('00000000-0000-0000-0002-000000000013', 'Spare Parts',       'spare-parts',     '00000000-0000-0000-0001-000000000002', 4, TRUE, now(), now(), 0)
ON CONFLICT (id) DO NOTHING;
