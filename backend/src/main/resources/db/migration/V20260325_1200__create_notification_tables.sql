-- Notification module schema
-- Stores all outbound notifications (SMS, email, in-app) for audit and delivery tracking.
-- No FK to auth_users to keep modules decoupled — user_id is a logical reference only.

CREATE TABLE ntf_notifications (
    id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID,                               -- logical ref to auth_users.id, nullable
    type        VARCHAR(30) NOT NULL,
    channel     VARCHAR(20) NOT NULL,
    recipient   VARCHAR(320) NOT NULL,              -- phone number or email address
    subject     VARCHAR(200),                       -- used for email channel only
    message     TEXT        NOT NULL,
    status      VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    error_message TEXT,                             -- populated on delivery failure
    sent_at     TIMESTAMPTZ,
    is_read     BOOLEAN     NOT NULL DEFAULT FALSE, -- for IN_APP channel
    read_at     TIMESTAMPTZ,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by  VARCHAR(100),
    version     BIGINT      NOT NULL DEFAULT 0,

    CONSTRAINT chk_ntf_type    CHECK (type    IN ('OTP_PHONE_VERIFICATION','OTP_EMAIL_VERIFICATION','OTP_PASSWORD_RESET','OTP_LOGIN','GENERAL')),
    CONSTRAINT chk_ntf_channel CHECK (channel IN ('SMS','EMAIL','IN_APP','PUSH')),
    CONSTRAINT chk_ntf_status  CHECK (status  IN ('PENDING','SENT','DELIVERED','FAILED'))
);

-- Lookup by user for in-app notification feed
CREATE INDEX idx_ntf_user_id       ON ntf_notifications (user_id)          WHERE user_id IS NOT NULL;
-- Filtered by channel (e.g. IN_APP feed for a user)
CREATE INDEX idx_ntf_user_channel  ON ntf_notifications (user_id, channel, created_at DESC) WHERE user_id IS NOT NULL;
-- Unread count query
CREATE INDEX idx_ntf_unread        ON ntf_notifications (user_id, channel)  WHERE is_read = FALSE AND user_id IS NOT NULL;
-- Delivery monitoring / admin queries
CREATE INDEX idx_ntf_status        ON ntf_notifications (status, created_at DESC);
