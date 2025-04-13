ALTER TABLE category
    ADD active BOOLEAN;

UPDATE category
    SET active = TRUE
    WHERE category.status = 'ACTIVE'
    AND category.active IS NULL;

UPDATE category
    SET active = FALSE
    WHERE category.status = 'INACTIVE'
    AND category.active IS NULL;