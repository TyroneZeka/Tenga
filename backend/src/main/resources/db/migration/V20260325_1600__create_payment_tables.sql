-- Payment module: pay_transactions and pay_wallets

CREATE TABLE pay_transactions (
    id                  UUID            NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    buyer_id            UUID            NOT NULL,
    seller_id           UUID            NOT NULL,
    listing_id          UUID            NOT NULL,
    amount              NUMERIC(14, 2)  NOT NULL,
    currency            VARCHAR(10)     NOT NULL CHECK (currency IN ('ZIG', 'USD')),
    payment_method      VARCHAR(10)     NOT NULL CHECK (payment_method IN ('ECOCASH', 'INNBUCKS')),
    status              VARCHAR(15)     NOT NULL DEFAULT 'PENDING'
                            CHECK (status IN ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED', 'REFUNDED')),
    payer_phone         VARCHAR(15),
    gateway_reference   TEXT            UNIQUE,
    failure_reason      TEXT,
    completed_at        TIMESTAMPTZ,
    refunded_at         TIMESTAMPTZ,
    created_at          TIMESTAMPTZ     NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ     NOT NULL DEFAULT now(),
    created_by          VARCHAR(255),
    version             BIGINT          NOT NULL DEFAULT 0
);

CREATE INDEX idx_pay_transactions_buyer    ON pay_transactions (buyer_id,  created_at DESC);
CREATE INDEX idx_pay_transactions_seller   ON pay_transactions (seller_id, created_at DESC);
CREATE INDEX idx_pay_transactions_listing  ON pay_transactions (listing_id);
CREATE INDEX idx_pay_transactions_status   ON pay_transactions (status) WHERE status IN ('PENDING', 'PROCESSING');

CREATE TABLE pay_wallets (
    id         UUID            NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id    UUID            NOT NULL,
    currency   VARCHAR(10)     NOT NULL CHECK (currency IN ('ZIG', 'USD')),
    balance    NUMERIC(14, 2)  NOT NULL DEFAULT 0.00 CHECK (balance >= 0),
    created_at TIMESTAMPTZ     NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ     NOT NULL DEFAULT now(),
    created_by VARCHAR(255),
    version    BIGINT          NOT NULL DEFAULT 0,
    CONSTRAINT uq_wallet_user_currency UNIQUE (user_id, currency)
);

CREATE INDEX idx_pay_wallets_user ON pay_wallets (user_id);
