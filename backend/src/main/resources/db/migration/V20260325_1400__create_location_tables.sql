-- Location module schema
-- Stores Zimbabwe city/suburb reference data and safe public meetup points.

CREATE TABLE loc_cities (
    id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(100) NOT NULL,
    province    VARCHAR(100) NOT NULL,
    latitude    DOUBLE PRECISION NOT NULL,
    longitude   DOUBLE PRECISION NOT NULL,
    active      BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by  VARCHAR(100),
    version     BIGINT      NOT NULL DEFAULT 0
);

CREATE UNIQUE INDEX idx_loc_cities_name ON loc_cities (lower(name));
CREATE INDEX        idx_loc_cities_active ON loc_cities (active) WHERE active = TRUE;

CREATE TABLE loc_safe_meetup_points (
    id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    city_id     UUID        NOT NULL REFERENCES loc_cities(id),
    name        VARCHAR(200) NOT NULL,
    description VARCHAR(500),
    address     VARCHAR(300),
    latitude    DOUBLE PRECISION NOT NULL,
    longitude   DOUBLE PRECISION NOT NULL,
    active      BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by  VARCHAR(100),
    version     BIGINT      NOT NULL DEFAULT 0
);

CREATE INDEX idx_loc_meetup_city ON loc_safe_meetup_points (city_id) WHERE active = TRUE;
