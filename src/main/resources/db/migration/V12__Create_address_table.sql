CREATE TABLE address(
    id UUID NOT NULL PRIMARY KEY,
    street VARCHAR(255),
    number VARCHAR(255),
    complement VARCHAR(255),
    neighborhood VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    zip_code VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

ALTER TABLE company
    ADD COLUMN address_id UUID REFERENCES address(id),
    ADD COLUMN delivery BOOLEAN,
    ADD COLUMN delivery_price DOUBLE PRECISION,
    ADD COLUMN delivery_radius INTEGER;

ALTER TABLE app_user
    ADD COLUMN address_id UUID REFERENCES address(id);
