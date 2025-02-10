ALTER TABLE food_item
    ADD IF NOT EXISTS image_id UUID CONSTRAINT FK_FOOD_ITEM_ON_IMAGE REFERENCES file_storage (id);

ALTER TABLE food_item_category
    ADD description VARCHAR(255);

ALTER TABLE food_item_category
    ADD "order" INTEGER;

ALTER TABLE food_item_category
    ADD required BOOLEAN;

ALTER TABLE food_item
    ADD "order" INTEGER;
