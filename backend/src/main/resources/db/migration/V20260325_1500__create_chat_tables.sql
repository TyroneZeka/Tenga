-- Chat module: cht_threads and cht_messages

CREATE TABLE cht_threads (
    id                  UUID         NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    buyer_id            UUID         NOT NULL,
    seller_id           UUID         NOT NULL,
    listing_id          UUID         NOT NULL,
    last_message_preview VARCHAR(200),
    buyer_unread_count  INTEGER      NOT NULL DEFAULT 0,
    seller_unread_count INTEGER      NOT NULL DEFAULT 0,
    created_at          TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ  NOT NULL DEFAULT now(),
    created_by          VARCHAR(255),
    version             BIGINT       NOT NULL DEFAULT 0,
    CONSTRAINT uq_thread_buyer_seller_listing UNIQUE (buyer_id, seller_id, listing_id)
);

CREATE INDEX idx_cht_threads_buyer  ON cht_threads (buyer_id,  updated_at DESC);
CREATE INDEX idx_cht_threads_seller ON cht_threads (seller_id, updated_at DESC);
CREATE INDEX idx_cht_threads_listing ON cht_threads (listing_id);

CREATE TABLE cht_messages (
    id           UUID         NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    thread_id    UUID         NOT NULL REFERENCES cht_threads (id) ON DELETE CASCADE,
    sender_id    UUID         NOT NULL,
    type         VARCHAR(10)  NOT NULL CHECK (type IN ('TEXT', 'IMAGE')),
    body         VARCHAR(2000),
    image_url    TEXT,
    status       VARCHAR(10)  NOT NULL DEFAULT 'SENT' CHECK (status IN ('SENT', 'DELIVERED', 'READ')),
    delivered_at TIMESTAMPTZ,
    read_at      TIMESTAMPTZ,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT now(),
    created_by   VARCHAR(255),
    version      BIGINT       NOT NULL DEFAULT 0
);

CREATE INDEX idx_cht_messages_thread_created ON cht_messages (thread_id, created_at DESC);
CREATE INDEX idx_cht_messages_unread ON cht_messages (thread_id, sender_id, status)
    WHERE status <> 'READ';
