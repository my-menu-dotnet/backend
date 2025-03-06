CREATE TABLE order_discount
(
    id         UUID NOT NULL,
    tenant_id  UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    discount   DOUBLE PRECISION,
    type       SMALLINT,
    start_at   date,
    end_at     date,
    CONSTRAINT pk_order_discount PRIMARY KEY (id)
);

ALTER TABLE order_item
    ADD discount_id UUID;

ALTER TABLE order_item
    ADD observation VARCHAR(255);

ALTER TABLE order_item
    ADD CONSTRAINT FK_ORDER_ITEM_ON_DISCOUNT FOREIGN KEY (discount_id) REFERENCES order_discount (id);