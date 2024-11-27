ALTER TABLE company_access
    ADD COLUMN ip_address VARCHAR(20),
    -- WTF?
    DROP CONSTRAINT fkamujyt0k3vkpoyvrgw6ky5v3l;
