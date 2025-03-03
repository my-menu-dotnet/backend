ALTER TABLE email_code
    ADD email VARCHAR(255);

DROP TABLE IF EXISTS food_order_foods CASCADE;

ALTER TABLE analytic_company_access
    DROP COLUMN IF EXISTS company_id;

ALTER TABLE discount
    DROP COLUMN IF EXISTS company_id;

ALTER TABLE discount
    DROP COLUMN IF EXISTS status;

ALTER TABLE food
    DROP COLUMN IF EXISTS company_id;

ALTER TABLE food_order
    DROP COLUMN iF EXISTS company_id;

ALTER TABLE refresh_token
    DROP COLUMN IF EXISTS tenant_id;

ALTER TABLE email_code
    DROP COLUMN IF EXISTS user_id;

DROP SEQUENCE IF EXISTS analytic_company_access_seq CASCADE;