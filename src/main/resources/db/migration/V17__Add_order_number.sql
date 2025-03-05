ALTER TABLE food_order
    DROP COLUMN IF EXISTS order_number;

ALTER TABLE food_order
    ADD order_number INTEGER NOT NULL DEFAULT floor(random() * 1000000);

CREATE UNIQUE INDEX idx_unique_order_number_per_tenant
    ON food_order (tenant_id, order_number);