ALTER TABLE food_order
    ADD address_id UUID;

ALTER TABLE food_order
    ADD company_observation VARCHAR(255);

ALTER TABLE food_order
    ADD user_name VARCHAR(255);

ALTER TABLE food_order
    ADD CONSTRAINT FK_FOOD_ORDER_ON_ADDRESS FOREIGN KEY (address_id) REFERENCES address (id);