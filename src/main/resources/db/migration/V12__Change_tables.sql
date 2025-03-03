ALTER TABLE order_item
    DROP CONSTRAINT fk_order_item_on_user;

ALTER TABLE discount
    DROP CONSTRAINT IF EXISTS fkd57ampu5md48xa965vycb2e8s;

ALTER TABLE food
    DROP CONSTRAINT IF EXISTS fkocyd43teypl75kkke4m6h31q6;

CREATE TABLE order_item_order_items
(
    order_item_id  UUID NOT NULL,
    order_items_id UUID NOT NULL
);

ALTER TABLE order_item
    ADD category VARCHAR(255);

ALTER TABLE food_order
    ADD user_id UUID;

ALTER TABLE order_item_order_items
    ADD CONSTRAINT uc_order_item_order_items_orderitems UNIQUE (order_items_id);

ALTER TABLE food_order
    ADD CONSTRAINT FK_FOOD_ORDER_ON_USER FOREIGN KEY (user_id) REFERENCES app_user (id);

ALTER TABLE order_item_order_items
    ADD CONSTRAINT fk_orditeordite_on_orderitem FOREIGN KEY (order_item_id) REFERENCES order_item (id);

ALTER TABLE order_item_order_items
    ADD CONSTRAINT fk_orditeordite_on_orderitems FOREIGN KEY (order_items_id) REFERENCES order_item (id);

ALTER TABLE order_item
    DROP COLUMN category_id;

ALTER TABLE order_item
    DROP COLUMN user_id;

ALTER TABLE analytic_company_access
    DROP COLUMN IF EXISTS company_id;

ALTER TABLE analytic_company_access
    DROP COLUMN id;

ALTER TABLE discount
    DROP COLUMN IF EXISTS company_id;

ALTER TABLE discount
    DROP COLUMN IF EXISTS status;

ALTER TABLE food
    DROP COLUMN IF EXISTS company_id;

ALTER TABLE food_order
    DROP COLUMN IF EXISTS company_id;

DROP SEQUENCE analytic_company_access_seq CASCADE;

ALTER TABLE analytic_company_access
    ADD id UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid();