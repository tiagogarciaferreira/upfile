CREATE
EXTENSION "uuid-ossp";

CREATE TABLE tb_users
(
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name          VARCHAR(255) NOT NULL,
    username      VARCHAR(50)  NOT NULL,
    email         VARCHAR(50)  NOT NULL,
    active        Boolean      NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    scopes        TEXT[]       NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE,
    updated_at    TIMESTAMP WITH TIME ZONE
);