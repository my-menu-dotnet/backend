-- Refresh token
ALTER TABLE refresh_token
    ALTER COLUMN created_at DROP NOT NULL;

ALTER TABLE refresh_token
    ADD updated_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE refresh_token
    ADD tenant_id UUID NOT NULL DEFAULT '80bf8ed5-759c-4f2d-8a0b-3a5a17415a1e';

-- Analytic company access
ALTER TABLE analytic_company_access
    RENAME COLUMN company_id TO tenant_id;

ALTER TABLE analytic_company_access
    ALTER COLUMN tenant_id SET NOT NULL;

ALTER TABLE analytic_company_access
    ADD COLUMN updated_at TIMESTAMP WITHOUT TIME ZONE;

-- Banner
ALTER TABLE banner
    RENAME COLUMN company_id TO tenant_id;

ALTER TABLE banner
    ALTER COLUMN tenant_id SET NOT NULL;

ALTER TABLE banner
    DROP CONSTRAINT fk_banner_on_companyid;

-- Category
ALTER TABLE category
    RENAME COLUMN company_id TO tenant_id;

ALTER TABLE category
    ALTER COLUMN tenant_id SET NOT NULL;

ALTER TABLE category
    DROP CONSTRAINT fk_category_on_company;

-- Discount
ALTER TABLE discount
    RENAME COLUMN company_id TO tenant_id;

ALTER TABLE discount
    ALTER COLUMN tenant_id SET NOT NULL;

ALTER TABLE discount
    DROP CONSTRAINT fk_discount_on_company;

-- Food Order
ALTER TABLE food_order
    RENAME COLUMN company_id TO tenant_id;

ALTER TABLE food_order
    ALTER COLUMN tenant_id SET NOT NULL;

ALTER TABLE food_order
    DROP CONSTRAINT fk_food_order_on_company;

-- Order Item
ALTER TABLE order_item
    RENAME COLUMN company_id TO tenant_id;

ALTER TABLE order_item
    ALTER COLUMN tenant_id SET NOT NULL;

ALTER TABLE order_item
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE order_item
    ADD updated_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE order_item
    DROP CONSTRAINT fk_order_item_on_company;

-- Food Item
ALTER TABLE food_item
    ADD tenant_id UUID NOT NULL DEFAULT '80bf8ed5-759c-4f2d-8a0b-3a5a17415a1e';

UPDATE food_item fi
SET tenant_id = c.tenant_id
FROM category c
WHERE fi.category_id = c.id;

-- Food Item Category
ALTER TABLE food_item_category
    ADD tenant_id UUID NOT NULL DEFAULT '80bf8ed5-759c-4f2d-8a0b-3a5a17415a1e';

UPDATE food_item_category fic
SET tenant_id = f.tenant_id
FROM food f
WHERE fic.food_id = f.id;

-- Address
ALTER TABLE address
    ADD tenant_id UUID NOT NULL DEFAULT '80bf8ed5-759c-4f2d-8a0b-3a5a17415a1e';

UPDATE address a
SET tenant_id = c.id
FROM company c
WHERE c.address_id = a.id;
