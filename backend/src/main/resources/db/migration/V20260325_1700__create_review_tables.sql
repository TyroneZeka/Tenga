-- Review module: rev_reviews

CREATE TABLE rev_reviews (
    id             UUID         NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    reviewee_id    UUID         NOT NULL,
    reviewer_id    UUID         NOT NULL,
    transaction_id UUID         NOT NULL UNIQUE,
    listing_id     UUID         NOT NULL,
    rating         SMALLINT     NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment        VARCHAR(1000),
    created_at     TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ  NOT NULL DEFAULT now(),
    created_by     VARCHAR(255),
    version        BIGINT       NOT NULL DEFAULT 0
);

CREATE INDEX idx_rev_reviews_reviewee ON rev_reviews (reviewee_id, created_at DESC);
CREATE INDEX idx_rev_reviews_reviewer ON rev_reviews (reviewer_id);
CREATE INDEX idx_rev_reviews_listing  ON rev_reviews (listing_id);
