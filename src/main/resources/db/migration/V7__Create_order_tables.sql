ALTER TABLE food_order_foods
    DROP CONSTRAINT fk_fooordfoo_on_food;

ALTER TABLE food_order_foods
    DROP CONSTRAINT fk_fooordfoo_on_order;

CREATE TABLE food_order_order_items
(
    order_id       UUID NOT NULL,
    order_items_id UUID NOT NULL
);

CREATE TABLE order_item
(
    id             UUID NOT NULL,
    title          VARCHAR(255),
    description    VARCHAR(255),
    picture_url_id UUID,
    category_id    VARCHAR(255),
    quantity       INTEGER,
    currency       VARCHAR(255),
    unit_price     DOUBLE PRECISION,
    user_id        UUID,
    company_id     UUID,
    CONSTRAINT pk_order_item PRIMARY KEY (id)
);

ALTER TABLE food_order_order_items
    ADD CONSTRAINT uc_food_order_order_items_orderitems UNIQUE (order_items_id);

ALTER TABLE food_order
    ADD CONSTRAINT FK_FOOD_ORDER_ON_COMPANY FOREIGN KEY (company_id) REFERENCES company (id);

ALTER TABLE order_item
    ADD CONSTRAINT FK_ORDER_ITEM_ON_COMPANY FOREIGN KEY (company_id) REFERENCES company (id);

ALTER TABLE order_item
    ADD CONSTRAINT FK_ORDER_ITEM_ON_PICTUREURL FOREIGN KEY (picture_url_id) REFERENCES file_storage (id);

ALTER TABLE order_item
    ADD CONSTRAINT FK_ORDER_ITEM_ON_USER FOREIGN KEY (user_id) REFERENCES app_user (id);

ALTER TABLE food_order_order_items
    ADD CONSTRAINT fk_fooordordite_on_order FOREIGN KEY (order_id) REFERENCES food_order (id);

ALTER TABLE food_order_order_items
    ADD CONSTRAINT fk_fooordordite_on_order_item FOREIGN KEY (order_items_id) REFERENCES order_item (id);

DROP TABLE food_order_foods CASCADE;