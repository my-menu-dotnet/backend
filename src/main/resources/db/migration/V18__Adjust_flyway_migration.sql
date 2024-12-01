ALTER TABLE analytic_company_access
    DROP CONSTRAINT analytic_company_access_company_id_fkey;

ALTER TABLE app_user
    DROP CONSTRAINT fk_user_role;

ALTER TABLE company_access
    DROP CONSTRAINT fki0e0l4gykaqhau22sif8dl3ho;

ALTER TABLE app_user
    ADD CONSTRAINT uc_app_user_address UNIQUE (address_id);

ALTER TABLE company
    ADD CONSTRAINT uc_company_address UNIQUE (address_id);

DROP TABLE company_access CASCADE;

DROP TABLE role CASCADE;

ALTER TABLE company_category
    DROP CONSTRAINT company_category_pkey;

ALTER TABLE user_company
    DROP CONSTRAINT user_company_pkey;

ALTER TABLE company
    DROP COLUMN created_by;

ALTER TABLE category
    DROP COLUMN image;

ALTER TABLE app_user
    DROP COLUMN role_id;

ALTER TABLE file_storage
    DROP COLUMN upload_dir;

DROP SEQUENCE company_access_seq CASCADE;

ALTER TABLE app_user
    ALTER COLUMN cpf DROP NOT NULL;

ALTER TABLE refresh_token
    ALTER COLUMN created_at DROP NOT NULL;

ALTER TABLE app_user
    ALTER COLUMN email DROP NOT NULL;

ALTER TABLE company
    ALTER COLUMN email DROP NOT NULL;

ALTER TABLE company
    ALTER COLUMN image_id DROP NOT NULL;

ALTER TABLE app_user
    ALTER COLUMN name DROP NOT NULL;

ALTER TABLE company
    ALTER COLUMN name DROP NOT NULL;

ALTER TABLE app_user
    ALTER COLUMN password DROP NOT NULL;

ALTER TABLE company
    ALTER COLUMN phone DROP NOT NULL;

ALTER TABLE refresh_token
    ALTER COLUMN user_id DROP NOT NULL;