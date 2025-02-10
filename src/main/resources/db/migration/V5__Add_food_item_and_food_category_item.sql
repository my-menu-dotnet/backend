CREATE TABLE food_item
(
    id             UUID NOT NULL,
    title          VARCHAR(255),
    desciption     VARCHAR(255),
    price_increase DOUBLE PRECISION,
    category_id    UUID,
    created_at     TIMESTAMP WITHOUT TIME ZONE,
    updated_at     TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_food_item PRIMARY KEY (id)
);

CREATE TABLE food_item_category
(
    id         UUID NOT NULL,
    title      VARCHAR(255),
    min_items  DOUBLE PRECISION,
    max_items  DOUBLE PRECISION,
    food_id    UUID,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_food_item_category PRIMARY KEY (id)
);

ALTER TABLE food_item_category
    ADD CONSTRAINT FK_FOOD_ITEM_CATEGORY_ON_FOOD FOREIGN KEY (food_id) REFERENCES food (id);

ALTER TABLE food_item
    ADD CONSTRAINT FK_FOOD_ITEM_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES food_item_category (id);

ALTER TABLE company
    DROP COLUMN IF EXISTS header_id;