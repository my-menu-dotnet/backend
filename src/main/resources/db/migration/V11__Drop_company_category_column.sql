ALTER TABLE category DROP COLUMN company_id;

ALTER TABLE food
    ADD COLUMN company_id UUID REFERENCES company(id);