ALTER TABLE food
    RENAME COLUMN company_id TO tenant_id;

ALTER TABLE food
    ALTER COLUMN tenant_id SET NOT NULL;

ALTER TABLE food
    DROP CONSTRAINT IF EXISTS fk_food_on_company;

DROP TABLE IF EXISTS food_order_foods CASCADE;

ALTER TABLE company
    DROP COLUMN IF EXISTS header_id;
