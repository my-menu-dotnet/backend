CREATE TABLE email_code
(
    id         INTEGER NOT NULL,
    user_id    UUID,
    code       VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_email_code PRIMARY KEY (id)
);
