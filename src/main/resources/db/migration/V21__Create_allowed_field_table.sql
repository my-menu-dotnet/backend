CREATE TABLE allowed_field
(
    id    UUID NOT NULL,
    field VARCHAR(255),
    CONSTRAINT pk_allowed_field PRIMARY KEY (id)
);

CREATE TABLE company_allowed_field
(
    allowed_field_id UUID NOT NULL,
    company_id       UUID NOT NULL
);

ALTER TABLE company_allowed_field
    ADD CONSTRAINT fk_comallfie_on_allowed_field FOREIGN KEY (allowed_field_id) REFERENCES allowed_field (id);

ALTER TABLE company_allowed_field
    ADD CONSTRAINT fk_comallfie_on_company FOREIGN KEY (company_id) REFERENCES company (id);