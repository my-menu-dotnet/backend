CREATE TABLE refresh_token (
    token varchar(255) PRIMARY KEY,
    user_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,

    FOREIGN KEY (user_id) REFERENCES app_user(id)
);