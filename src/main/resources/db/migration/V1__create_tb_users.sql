CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE tb_users
(
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name          VARCHAR(255)       NOT NULL,
    username      VARCHAR(50) UNIQUE NOT NULL,
    email         VARCHAR(50) UNIQUE NOT NULL,
    active        Boolean            NOT NULL,
    password_hash VARCHAR(255)       NOT NULL,
    scopes        TEXT[]       NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE,
    updated_at    TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_users_email ON tb_users (email);
CREATE INDEX idx_users_username ON tb_users (username);
CREATE INDEX idx_users_active ON tb_users (active);