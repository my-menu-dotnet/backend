ALTER TABLE category
    DROP CONSTRAINT fk_category_on_image;

ALTER TABLE company_categories
    DROP CONSTRAINT fk_comcat_on_category;

ALTER TABLE company_categories
    DROP CONSTRAINT fk_comcat_on_company;

CREATE TABLE discount
(
    id         UUID NOT NULL,
    food_id    UUID,
    company_id UUID,
    discount   DOUBLE PRECISION,
    type       VARCHAR(255),
    start_at   date,
    end_at     date,
    status     VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_discount PRIMARY KEY (id)
);

CREATE TABLE food_order
(
    id           UUID NOT NULL,
    table_number INTEGER,
    total_price  DOUBLE PRECISION,
    status       VARCHAR(255),
    company_id   UUID,
    created_at   TIMESTAMP WITHOUT TIME ZONE,
    updated_at   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_food_order PRIMARY KEY (id)
);

CREATE TABLE food_order_foods
(
    order_id UUID NOT NULL,
    foods_id UUID NOT NULL
);

ALTER TABLE category
    ADD company_id UUID;

ALTER TABLE category
    ADD "order" INTEGER DEFAULT 0;

ALTER TABLE category
    ALTER COLUMN company_id SET NOT NULL;

ALTER TABLE company
    ADD description VARCHAR(255);

ALTER TABLE company
    ADD primary_color VARCHAR(255);

ALTER TABLE company
    ADD url VARCHAR(255);

ALTER TABLE food_order_foods
    ADD CONSTRAINT uc_food_order_foods_foods UNIQUE (foods_id);

ALTER TABLE category
    ADD CONSTRAINT FK_CATEGORY_ON_COMPANY FOREIGN KEY (company_id) REFERENCES company (id);

ALTER TABLE discount
    ADD CONSTRAINT FK_DISCOUNT_ON_COMPANY FOREIGN KEY (company_id) REFERENCES company (id);

ALTER TABLE discount
    ADD CONSTRAINT FK_DISCOUNT_ON_FOOD FOREIGN KEY (food_id) REFERENCES food (id);

ALTER TABLE food_order_foods
    ADD CONSTRAINT fk_fooordfoo_on_food FOREIGN KEY (foods_id) REFERENCES food (id);

ALTER TABLE food_order_foods
    ADD CONSTRAINT fk_fooordfoo_on_order FOREIGN KEY (order_id) REFERENCES food_order (id);

DROP TABLE company_categories CASCADE;

ALTER TABLE category
    DROP COLUMN description;

ALTER TABLE category
    DROP COLUMN image_id;