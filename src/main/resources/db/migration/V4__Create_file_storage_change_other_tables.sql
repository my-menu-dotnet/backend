CREATE TABLE category
(
    id          UUID NOT NULL,
    name        VARCHAR(255),
    description VARCHAR(255),
    image       VARCHAR(255),
    status      VARCHAR(20),
    company_id  UUID,

    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT uc_category_name UNIQUE (name),
    CONSTRAINT uc_category_image UNIQUE (image),

    CONSTRAINT fk_category_company FOREIGN KEY (company_id) REFERENCES company (id)
);

CREATE TABLE file_storage
(
    id             UUID NOT NULL,
    file_name      VARCHAR(255),
    file_type      VARCHAR(255),
    size           BIGINT,
    url            VARCHAR(255),
    company_id     UUID,
    user_id        UUID,

    CONSTRAINT pk_file_storage PRIMARY KEY (id),
    CONSTRAINT uc_file_storage_file_name UNIQUE (file_name),
    CONSTRAINT uc_file_storage_url UNIQUE (url),

    CONSTRAINT fk_file_storage_company FOREIGN KEY (company_id) REFERENCES company (id),
    CONSTRAINT fk_file_storage_user FOREIGN KEY (user_id) REFERENCES app_user (id)
);

CREATE TABLE food
(
    id           UUID NOT NULL,
    name         VARCHAR(255),
    description  VARCHAR(255),
    price        DOUBLE PRECISION,
    image_id     UUID,
    status       VARCHAR(20),
    lactose_free BOOLEAN,
    gluten_free  BOOLEAN,
    vegan        BOOLEAN,
    vegetarian   BOOLEAN,
    halal        BOOLEAN,
    category_id  UUID,

    CONSTRAINT pk_food PRIMARY KEY (id),
    CONSTRAINT uc_food_name UNIQUE (name),
    CONSTRAINT uc_food_image UNIQUE (image_id),

    CONSTRAINT fk_food_category FOREIGN KEY (category_id) REFERENCES category (id),
    CONSTRAINT fk_food_file_storage FOREIGN KEY (image_id) REFERENCES file_storage (id)
);
