ALTER TABLE company
    ADD COLUMN image_id UUID,

    ALTER COLUMN cnpj DROP NOT NULL,
    ALTER COLUMN phone SET NOT NULL,

    ADD CONSTRAINT fk_company_image FOREIGN KEY (image_id) REFERENCES file_storage(id),
    ADD CONSTRAINT uc_company_image UNIQUE (image_id);