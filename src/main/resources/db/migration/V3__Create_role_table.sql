CREATE TABLE role (
    id UUID PRIMARY KEY,
    name VARCHAR(24) NOT NULL
);

INSERT INTO
    role (id, name)
VALUES
    ('8526f20e-5ba5-4ef5-9210-53acccb05ad4', 'MOD'),
    ('64b32bad-33f4-4b96-ab77-22932d42a147', 'ADMIN');

ALTER TABLE user_company
    DROP COLUMN user_role;

ALTER TABLE app_user
    ADD COLUMN role_id UUID NOT NULL default '64b32bad-33f4-4b96-ab77-22932d42a147',
    ADD CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES role (id);