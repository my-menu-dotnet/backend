-- Link manual orders to the client (customer) when one is registered.
-- The client table itself was created in V23.
ALTER TABLE food_order
    ADD client_id UUID;

ALTER TABLE food_order
    ADD CONSTRAINT fk_food_order_on_client FOREIGN KEY (client_id) REFERENCES client (id) ON DELETE SET NULL;

CREATE INDEX idx_food_order_client ON food_order (client_id);
