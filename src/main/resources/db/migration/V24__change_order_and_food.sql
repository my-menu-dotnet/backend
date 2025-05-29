ALTER TABLE order_item
    DROP CONSTRAINT fk_order_item_on_image;

ALTER TABLE food_order
    ADD delivery_fee INTEGER;

ALTER TABLE order_item
    ADD food_id UUID;

ALTER TABLE order_item
    ADD CONSTRAINT FK_ORDER_ITEM_ON_FOOD FOREIGN KEY (food_id) REFERENCES food (id);

ALTER TABLE order_item
    DROP COLUMN category;

ALTER TABLE order_item
    DROP COLUMN description;

ALTER TABLE order_item
    DROP COLUMN image_id;

ALTER TABLE food_order
    DROP COLUMN table_number;