-- Auth module tables
-- Schema prefix: auth_

CREATE TABLE auth_users (
    id            UUID         NOT NULL DEFAULT gen_random_uuid(),
    email         VARCHAR(100),
    phone_number  VARCHAR(20),
    password_hash VARCHAR(120),
    role          VARCHAR(20)  NOT NULL,
    provider      VARCHAR(20)  NOT NULL,
    provider_id   VARCHAR(200),
    email_verified  BOOLEAN    NOT NULL DEFAULT FALSE,
    phone_verified  BOOLEAN    NOT NULL DEFAULT FALSE,
    enabled       BOOLEAN      NOT NULL DEFAULT TRUE,
    last_login_at TIMESTAMPTZ,
    deleted_at    TIMESTAMPTZ,
    -- BaseEntity audit fields
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    created_by    VARCHAR(100),
    version       BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT auth_users_pk PRIMARY KEY (id),
    CONSTRAINT auth_users_email_unique UNIQUE (email),
    CONSTRAINT auth_users_phone_unique UNIQUE (phone_number),
    CONSTRAINT auth_users_role_check CHECK (role IN ('BUYER','SELLER','ADMIN','MODERATOR')),
    CONSTRAINT auth_users_provider_check CHECK (provider IN ('LOCAL','GOOGLE','APPLE'))
);

CREATE INDEX idx_auth_users_email ON auth_users (email) WHERE deleted_at IS NULL;
CREATE INDEX idx_auth_users_phone ON auth_users (phone_number) WHERE deleted_at IS NULL;

-- ---------------------------------------------------------------------------

CREATE TABLE auth_refresh_tokens (
    id          UUID         NOT NULL DEFAULT gen_random_uuid(),
    token       VARCHAR(512) NOT NULL,
    user_id     UUID         NOT NULL,
    expires_at  TIMESTAMPTZ  NOT NULL,
    revoked     BOOLEAN      NOT NULL DEFAULT FALSE,
    device_info VARCHAR(50),
    -- BaseEntity audit fields
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    created_by  VARCHAR(100),
    version     BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT auth_refresh_tokens_pk PRIMARY KEY (id),
    CONSTRAINT auth_refresh_tokens_token_unique UNIQUE (token),
    CONSTRAINT auth_refresh_tokens_user_fk FOREIGN KEY (user_id) REFERENCES auth_users (id)
);

CREATE INDEX idx_auth_refresh_tokens_token ON auth_refresh_tokens (token);
CREATE INDEX idx_auth_refresh_tokens_user  ON auth_refresh_tokens (user_id);
CREATE INDEX idx_auth_refresh_tokens_expiry ON auth_refresh_tokens (expires_at) WHERE revoked = FALSE;

-- ---------------------------------------------------------------------------

CREATE TABLE auth_otp_codes (
    id          UUID        NOT NULL DEFAULT gen_random_uuid(),
    recipient   VARCHAR(20) NOT NULL,
    code        VARCHAR(10) NOT NULL,
    purpose     VARCHAR(30) NOT NULL,
    expires_at  TIMESTAMPTZ NOT NULL,
    used        BOOLEAN     NOT NULL DEFAULT FALSE,
    attempts    INT         NOT NULL DEFAULT 0,
    -- BaseEntity audit fields
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by  VARCHAR(100),
    version     BIGINT      NOT NULL DEFAULT 0,

    CONSTRAINT auth_otp_codes_pk PRIMARY KEY (id),
    CONSTRAINT auth_otp_codes_purpose_check CHECK (
        purpose IN ('PHONE_VERIFICATION','EMAIL_VERIFICATION','PASSWORD_RESET','LOGIN')
    )
);

CREATE INDEX idx_auth_otp_recipient_purpose ON auth_otp_codes (recipient, purpose, expires_at)
    WHERE used = FALSE;
