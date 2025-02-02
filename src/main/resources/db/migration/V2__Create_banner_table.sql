CREATE TABLE banner
(
    id            UUID NOT NULL,
    image_id      UUID,
    redirect      VARCHAR(255),
    url           VARCHAR(255),
    "order"       INTEGER,
    active        BOOLEAN,
    company_id    UUID,
    created_at    TIMESTAMP WITHOUT TIME ZONE,
    updated_at    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_banner PRIMARY KEY (id)
);

ALTER TABLE banner
    ADD CONSTRAINT FK_BANNER_ON_COMPANYID FOREIGN KEY (company_id) REFERENCES company (id);

ALTER TABLE banner
    ADD CONSTRAINT FK_BANNER_ON_IMAGE FOREIGN KEY (image_id) REFERENCES file_storage (id);