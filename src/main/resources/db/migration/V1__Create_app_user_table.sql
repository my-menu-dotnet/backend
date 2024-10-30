CREATE TABLE "app_user"
(
    id       UUID PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    cpf      VARCHAR(11) NOT NULL,
    phone    VARCHAR(20) NULL,
    password VARCHAR(60) NOT NULL,
    role     VARCHAR(20) NOT NULL default 'USER',

    CONSTRAINT uc_user_cpf UNIQUE (cpf),
    CONSTRAINT uc_user_email UNIQUE (email),
    CONSTRAINT role_check CHECK (role IN ('MOD', 'ADMIN'))
);
