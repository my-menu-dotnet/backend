ALTER TABLE email_code
    ADD email VARCHAR(255);

DROP TABLE IF EXISTS food_order_foods CASCADE;

ALTER TABLE analytic_company_access
    DROP COLUMN company_id;

ALTER TABLE discount
    DROP COLUMN company_id;

ALTER TABLE discount
    DROP COLUMN status;

ALTER TABLE food
    DROP COLUMN company_id;

ALTER TABLE food_order
    DROP COLUMN company_id;

ALTER TABLE refresh_token
    DROP COLUMN tenant_id;

ALTER TABLE email_code
    DROP COLUMN user_id;

DROP SEQUENCE IF EXISTS analytic_company_access_seq CASCADE;