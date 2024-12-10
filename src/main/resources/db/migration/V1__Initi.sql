CREATE SEQUENCE IF NOT EXISTS analytic_company_access_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS email_code_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE address
(
    id           UUID NOT NULL,
    street       VARCHAR(255),
    number       VARCHAR(255),
    complement   VARCHAR(255),
    neighborhood VARCHAR(255),
    city         VARCHAR(255),
    state        VARCHAR(255),
    zip_code     VARCHAR(255),
    latitude     DOUBLE PRECISION,
    longitude    DOUBLE PRECISION,
    created_at   TIMESTAMP WITHOUT TIME ZONE,
    updated_at   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_address PRIMARY KEY (id)
);

CREATE TABLE allowed_field
(
    id    UUID NOT NULL,
    field VARCHAR(255),
    CONSTRAINT pk_allowed_field PRIMARY KEY (id)
);

CREATE TABLE analytic_company_access
(
    id             INTEGER NOT NULL,
    company_id     UUID,
    ip             VARCHAR(255),
    user_agent     VARCHAR(255),
    access_way     VARCHAR(255),
    accessed_at    TIMESTAMP WITHOUT TIME ZONE,
    time_on_page   INTEGER,
    contacted      BOOLEAN,
    contacted_at   TIMESTAMP WITHOUT TIME ZONE,
    contacted_from VARCHAR(255),
    created_at     TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_analytic_company_access PRIMARY KEY (id)
);

CREATE TABLE app_user
(
    id                  UUID NOT NULL,
    name                VARCHAR(255),
    email               VARCHAR(255),
    cpf                 VARCHAR(255),
    phone               VARCHAR(255),
    password            VARCHAR(255),
    address_id          UUID,
    is_verified_email   BOOLEAN DEFAULT FALSE,
    is_active           BOOLEAN DEFAULT TRUE,
    last_password_reset TIMESTAMP WITHOUT TIME ZONE,
    last_login          TIMESTAMP WITHOUT TIME ZONE,
    created_at          TIMESTAMP WITHOUT TIME ZONE,
    updated_at          TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_app_user PRIMARY KEY (id)
);

CREATE TABLE app_user_companies
(
    user_id      UUID NOT NULL,
    companies_id UUID NOT NULL
);

CREATE TABLE category
(
    id          UUID NOT NULL,
    name        VARCHAR(255),
    description VARCHAR(255),
    image_id    UUID,
    status      VARCHAR(255),
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE company
(
    id                UUID                       NOT NULL,
    name              VARCHAR(255),
    cnpj              VARCHAR(255),
    phone             VARCHAR(255),
    email             VARCHAR(255),
    is_verified_email BOOLEAN      DEFAULT FALSE,
    image_id          UUID,
    delivery          BOOLEAN      DEFAULT FALSE NOT NULL,
    delivery_price    DOUBLE PRECISION,
    delivery_radius   INTEGER,
    address_id        UUID,
    status            VARCHAR(255) DEFAULT 'ACTIVE',
    created_at        TIMESTAMP WITHOUT TIME ZONE,
    updated_at        TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_company PRIMARY KEY (id)
);

CREATE TABLE company_allowed_field
(
    company_id       UUID NOT NULL,
    allowed_field_id UUID NOT NULL
);

CREATE TABLE company_categories
(
    company_id    UUID NOT NULL,
    categories_id UUID NOT NULL
);

CREATE TABLE email_code
(
    id         INTEGER NOT NULL,
    user_id    UUID,
    type       VARCHAR(255),
    code       VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_email_code PRIMARY KEY (id)
);

CREATE TABLE file_storage
(
    id        UUID NOT NULL,
    file_name VARCHAR(255),
    file_type VARCHAR(255),
    size      BIGINT,
    CONSTRAINT pk_file_storage PRIMARY KEY (id)
);

CREATE TABLE food
(
    id           UUID NOT NULL,
    name         VARCHAR(255),
    description  VARCHAR(255),
    price        DOUBLE PRECISION,
    image_id     UUID,
    status       VARCHAR(255),
    lactose_free BOOLEAN,
    gluten_free  BOOLEAN,
    vegan        BOOLEAN,
    vegetarian   BOOLEAN,
    halal        BOOLEAN,
    category_id  UUID,
    company_id   UUID,
    created_at   TIMESTAMP WITHOUT TIME ZONE,
    updated_at   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_food PRIMARY KEY (id)
);

CREATE TABLE refresh_token
(
    token      VARCHAR(255)                NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id    UUID                        NOT NULL,
    CONSTRAINT pk_refresh_token PRIMARY KEY (token)
);

ALTER TABLE app_user
    ADD CONSTRAINT uc_app_user_address UNIQUE (address_id);

ALTER TABLE company
    ADD CONSTRAINT uc_company_address UNIQUE (address_id);

ALTER TABLE food
    ADD CONSTRAINT uc_food_image UNIQUE (image_id);

CREATE INDEX email_code_code_index ON email_code (code);

CREATE INDEX email_code_type_index ON email_code (type);

CREATE INDEX email_code_user_id_index ON email_code (user_id);

CREATE INDEX idx_company_cnpj ON company (cnpj);

CREATE INDEX idx_company_email ON company (email);

CREATE INDEX idx_company_name ON company (name);

CREATE INDEX idx_company_status ON company (status);

CREATE INDEX idx_user_cpf ON app_user (cpf);

CREATE INDEX idx_user_email ON app_user (email);

CREATE INDEX idx_user_phone ON app_user (phone);

ALTER TABLE app_user
    ADD CONSTRAINT FK_APP_USER_ON_ADDRESS FOREIGN KEY (address_id) REFERENCES address (id);

ALTER TABLE category
    ADD CONSTRAINT FK_CATEGORY_ON_IMAGE FOREIGN KEY (image_id) REFERENCES file_storage (id);

ALTER TABLE company
    ADD CONSTRAINT FK_COMPANY_ON_ADDRESS FOREIGN KEY (address_id) REFERENCES address (id);

ALTER TABLE company
    ADD CONSTRAINT FK_COMPANY_ON_IMAGE FOREIGN KEY (image_id) REFERENCES file_storage (id);

ALTER TABLE food
    ADD CONSTRAINT FK_FOOD_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE food
    ADD CONSTRAINT FK_FOOD_ON_COMPANY FOREIGN KEY (company_id) REFERENCES company (id);

ALTER TABLE food
    ADD CONSTRAINT FK_FOOD_ON_IMAGE FOREIGN KEY (image_id) REFERENCES file_storage (id);

ALTER TABLE app_user_companies
    ADD CONSTRAINT fk_appusecom_on_company FOREIGN KEY (companies_id) REFERENCES company (id);

ALTER TABLE app_user_companies
    ADD CONSTRAINT fk_appusecom_on_user FOREIGN KEY (user_id) REFERENCES app_user (id);

ALTER TABLE company_allowed_field
    ADD CONSTRAINT fk_comallfie_on_allowed_field FOREIGN KEY (allowed_field_id) REFERENCES allowed_field (id);

ALTER TABLE company_allowed_field
    ADD CONSTRAINT fk_comallfie_on_company FOREIGN KEY (company_id) REFERENCES company (id);

ALTER TABLE company_categories
    ADD CONSTRAINT fk_comcat_on_category FOREIGN KEY (categories_id) REFERENCES category (id);

ALTER TABLE company_categories
    ADD CONSTRAINT fk_comcat_on_company FOREIGN KEY (company_id) REFERENCES company (id);