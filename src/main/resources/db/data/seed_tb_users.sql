-- =============================================================================
-- Seed: tb_users - 2 user records
-- Read: 1 | Read/Write: 1
-- Note: id, created_at, updated_at are auto-generated
-- =============================================================================

DELETE
FROM tb_users;

INSERT INTO tb_users (name, username, email, active, password_hash, scopes)
VALUES ('Read User',
        'user_read',
        'read@files.tgfcodes.com',
        true,
        '$argon2id$v=19$m=16384,t=2,p=1$/li6ybP1l5x/cB3k25YGAQ$b9bnckC79GbVsVw9+1yy8Ikvp9DKczcD0Wn6hyrdYtw',
        '{"files:read"}'),

       ('Admin User',
        'user_admin',
        'admin@files.tgfcodes.com',
        true,
        '$argon2id$v=19$m=16384,t=2,p=1$ciCZacWcXsVLV/Oea4AZYg$IFdiZy4cgx5LPYsP8V03ar5Ytav4W8SuOiD5XVWbHys',
        '{"files:read", "files:write"}');
