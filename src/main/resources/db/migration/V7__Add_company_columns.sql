ALTER TABLE company
    ADD COLUMN status     VARCHAR(255) DEFAULT 'ACTIVE',
    ADD COLUMN created_by UUID;

CREATE TABLE company_category
(
    company_id  UUID,
    category_id UUID,
    PRIMARY KEY (company_id, category_id)
);