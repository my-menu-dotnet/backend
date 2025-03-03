ALTER TABLE address
    ADD validated BOOLEAN;

ALTER TABLE app_user
    ALTER COLUMN email SET NOT NULL;