CREATE TABLE idempotency_keys (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    idempotency_key VARCHAR(100) NOT NULL,
    user_id UUID NOT NULL REFERENCES users(id),
    endpoint VARCHAR(200) NOT NULL,
    response_status INT NOT NULL,
    response_body TEXT NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_idempotency_keys_endpoint_not_blank CHECK (length(trim(endpoint)) > 0),
    CONSTRAINT chk_idempotency_keys_response_status CHECK (response_status BETWEEN 100 AND 599),
    CONSTRAINT chk_idempotency_keys_expires_after_created CHECK (expires_at > created_at)
);

CREATE UNIQUE INDEX uq_idempotency_keys_key_user_endpoint
    ON idempotency_keys(idempotency_key, user_id, endpoint);
