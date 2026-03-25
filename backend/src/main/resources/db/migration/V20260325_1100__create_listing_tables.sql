-- Listing module tables
-- Schema prefix: lst_

CREATE TABLE lst_categories (
    id         UUID        NOT NULL DEFAULT gen_random_uuid(),
    name       VARCHAR(100) NOT NULL,
    slug       VARCHAR(100),
    icon_url   VARCHAR(255),
    parent_id  UUID,
    sort_order INT         NOT NULL DEFAULT 0,
    active     BOOLEAN     NOT NULL DEFAULT TRUE,
    -- BaseEntity audit fields
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by VARCHAR(100),
    version    BIGINT      NOT NULL DEFAULT 0,

    CONSTRAINT lst_categories_pk PRIMARY KEY (id),
    CONSTRAINT lst_categories_name_unique UNIQUE (name),
    CONSTRAINT lst_categories_parent_fk FOREIGN KEY (parent_id) REFERENCES lst_categories (id)
);

CREATE INDEX idx_lst_categories_parent ON lst_categories (parent_id);
CREATE INDEX idx_lst_categories_slug ON lst_categories (slug);

-- ---------------------------------------------------------------------------

CREATE TABLE lst_listings (
    id          UUID         NOT NULL DEFAULT gen_random_uuid(),
    title       VARCHAR(150) NOT NULL,
    description TEXT         NOT NULL,
    price       NUMERIC(15,2) NOT NULL,
    currency    VARCHAR(10)  NOT NULL,
    condition   VARCHAR(20)  NOT NULL,
    status      VARCHAR(20)  NOT NULL DEFAULT 'DRAFT',
    seller_id   UUID         NOT NULL,
    category_id UUID         NOT NULL,
    city        VARCHAR(100),
    suburb      VARCHAR(100),
    latitude    DOUBLE PRECISION,
    longitude   DOUBLE PRECISION,
    view_count  INT          NOT NULL DEFAULT 0,
    negotiable  BOOLEAN      NOT NULL DEFAULT FALSE,
    expires_at  TIMESTAMPTZ,
    deleted_at  TIMESTAMPTZ,
    -- BaseEntity audit fields
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    created_by  VARCHAR(100),
    version     BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT lst_listings_pk PRIMARY KEY (id),
    CONSTRAINT lst_listings_category_fk FOREIGN KEY (category_id) REFERENCES lst_categories (id),
    CONSTRAINT lst_listings_currency_check CHECK (currency IN ('ZIG','USD')),
    CONSTRAINT lst_listings_condition_check CHECK (condition IN ('NEW','LIKE_NEW','GOOD','FAIR','POOR')),
    CONSTRAINT lst_listings_status_check CHECK (
        status IN ('DRAFT','ACTIVE','SOLD','RESERVED','EXPIRED','REMOVED')
    ),
    CONSTRAINT lst_listings_price_positive CHECK (price > 0)
);

CREATE INDEX idx_lst_listings_seller    ON lst_listings (seller_id) WHERE deleted_at IS NULL;
CREATE INDEX idx_lst_listings_category  ON lst_listings (category_id) WHERE deleted_at IS NULL;
CREATE INDEX idx_lst_listings_status    ON lst_listings (status) WHERE deleted_at IS NULL;
CREATE INDEX idx_lst_listings_city      ON lst_listings (city) WHERE status = 'ACTIVE' AND deleted_at IS NULL;
CREATE INDEX idx_lst_listings_price     ON lst_listings (price, currency) WHERE status = 'ACTIVE' AND deleted_at IS NULL;
CREATE INDEX idx_lst_listings_created   ON lst_listings (created_at DESC) WHERE status = 'ACTIVE' AND deleted_at IS NULL;
-- Geospatial index for proximity queries
CREATE INDEX idx_lst_listings_location  ON lst_listings (latitude, longitude) WHERE status = 'ACTIVE' AND deleted_at IS NULL;

-- ---------------------------------------------------------------------------

CREATE TABLE lst_listing_images (
    id          UUID         NOT NULL DEFAULT gen_random_uuid(),
    listing_id  UUID         NOT NULL,
    storage_key VARCHAR(512) NOT NULL,
    url         VARCHAR(512) NOT NULL,
    sort_order  INT          NOT NULL DEFAULT 0,
    -- BaseEntity audit fields
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    created_by  VARCHAR(100),
    version     BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT lst_listing_images_pk PRIMARY KEY (id),
    CONSTRAINT lst_listing_images_listing_fk FOREIGN KEY (listing_id) REFERENCES lst_listings (id) ON DELETE CASCADE
);

CREATE INDEX idx_lst_listing_images_listing ON lst_listing_images (listing_id);
