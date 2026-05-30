-- =============================================================================
-- Seed: tb_users - 2 user records
-- Read: 1 | Read/Write: 1
-- Note: id, created_at, updated_at are auto-generated
-- =============================================================================

DELETE
FROM tb_users;

DELETE
FROM tb_stored_files;

INSERT INTO tb_users (name, username, email, active, password_hash, scopes)
VALUES ('Read User',
        'user_read',
        'read@files.tgfcodes.com',
        true,
        '$argon2id$v=19$m=16384,t=2,p=1$QSQfU6l8MBgAGVAePW2W1Q$THxacIqWgdzmH2YJVXkre2Kgit0U6Cyw6avNpOlcqCw',
        '{"files:read"}'),

       ('Admin User',
        'user_admin',
        'admin@files.tgfcodes.com',
        true,
        '$argon2id$v=19$m=16384,t=2,p=1$PKKS5bKT78agZ/GTlOGB6g$6Jsg6w3O62fJ6x4bh3UOWL0B5a30QYV/tBv0u8kl1sQ',
        '{"files:read", "files:write"}');
