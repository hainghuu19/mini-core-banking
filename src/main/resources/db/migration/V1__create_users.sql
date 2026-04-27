CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE users (
                       id               UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
                       email            VARCHAR(255) NOT NULL UNIQUE,
                       password_hash    VARCHAR(255) NOT NULL,
                       full_name        VARCHAR(100) NOT NULL,
                       role             VARCHAR(20)  NOT NULL DEFAULT 'ROLE_USER',
                       status           VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
                       failed_login_attempts INT     NOT NULL DEFAULT 0,
                       locked_until     TIMESTAMPTZ,
                       created_at       TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
                       updated_at       TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_users_email ON users(email);