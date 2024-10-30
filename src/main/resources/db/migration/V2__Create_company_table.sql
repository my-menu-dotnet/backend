CREATE TABLE "company"
(
    id    UUID PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    cnpj  VARCHAR(14)  NOT NULL,
    phone VARCHAR(20)  NULL,
    email VARCHAR(255) NOT NULL,

    CONSTRAINT uc_company_cnpj UNIQUE (cnpj),
    CONSTRAINT uc_company_email UNIQUE (email)
);

CREATE TABLE user_company
(
    user_id    UUID        NOT NULL,
    company_id UUID        NOT NULL,
    user_role       VARCHAR(20) NOT NULL default 'MOD',

    PRIMARY KEY (user_id, company_id),
    FOREIGN KEY (user_id) REFERENCES app_user (id),
    FOREIGN KEY (company_id) REFERENCES company (id),
    CONSTRAINT role_check CHECK (user_role IN ('MOD', 'ADMIN'))
);

ALTER TABLE app_user
    DROP COLUMN role;