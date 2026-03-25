-- User module schema
-- Stores public profile data for each registered user.
-- user_id is a logical reference to auth_users.id — no FK to keep modules decoupled.

CREATE TABLE usr_profiles (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID            NOT NULL UNIQUE,
    display_name    VARCHAR(100),
    bio             TEXT,
    avatar_storage_key VARCHAR(500),
    avatar_url      VARCHAR(500),
    city            VARCHAR(100),
    suburb          VARCHAR(100),
    trust_score     DECIMAL(3, 2),
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
    created_by      VARCHAR(100),
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT chk_usr_trust_score CHECK (trust_score IS NULL OR (trust_score >= 0 AND trust_score <= 5))
);

CREATE INDEX idx_usr_profiles_user_id ON usr_profiles (user_id);
