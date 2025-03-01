ALTER TABLE company
    ADD header_id UUID;

ALTER TABLE company
    ADD CONSTRAINT FK_COMPANY_ON_HEADER FOREIGN KEY (header_id) REFERENCES file_storage (id);