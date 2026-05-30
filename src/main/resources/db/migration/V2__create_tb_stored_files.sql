CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE tb_stored_files
(
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    bucket              VARCHAR(100)        NOT NULL,
    file_name           VARCHAR(255)        NOT NULL,
    key                 VARCHAR(500) UNIQUE NOT NULL,
    e_tag               VARCHAR(100) UNIQUE NOT NULL,
    hash                VARCHAR(255) UNIQUE NOT NULL,
    extension           VARCHAR(20)         NOT NULL,
    content_type        VARCHAR(50)         NOT NULL,
    content_disposition VARCHAR(20)         NOT NULL,
    size                BIGINT              NOT NULL,
    mime_type           VARCHAR(50)         NOT NULL,
    type                VARCHAR(50)         NOT NULL,
    created_at          TIMESTAMP WITH TIME ZONE,
    created_by_user_id  UUID                NOT NULL,

    CONSTRAINT fk_stored_files_user
        FOREIGN KEY (created_by_user_id)
            REFERENCES tb_users (id)
            ON DELETE RESTRICT
            ON UPDATE CASCADE
);

CREATE INDEX idx_stored_files_user_id ON tb_stored_files (created_by_user_id);
CREATE INDEX idx_stored_files_bucket_key ON tb_stored_files (bucket, key);
CREATE INDEX idx_stored_files_type ON tb_stored_files (type);
CREATE INDEX idx_stored_files_created_at ON tb_stored_files (created_at);