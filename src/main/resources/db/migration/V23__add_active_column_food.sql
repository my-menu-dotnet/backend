ALTER TABLE food
    ADD active BOOLEAN;

UPDATE food
SET active = TRUE
WHERE food.status = 'ACTIVE'
  AND food.active IS NULL;

UPDATE food
SET active = FALSE
WHERE food.status = 'INACTIVE'
  AND food.active IS NULL;

ALTER TABLE category
    DROP COLUMN status;

ALTER TABLE food
    DROP COLUMN status;