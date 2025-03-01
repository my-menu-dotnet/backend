ALTER TABLE order_item
    DROP CONSTRAINT fk_order_item_on_pictureurl;

ALTER TABLE order_item
    ADD image_id UUID;

ALTER TABLE order_item
    ADD CONSTRAINT FK_ORDER_ITEM_ON_IMAGE FOREIGN KEY (image_id) REFERENCES file_storage (id);

ALTER TABLE order_item
    DROP COLUMN picture_url_id;