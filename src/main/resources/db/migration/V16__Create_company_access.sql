CREATE TABLE analytic_company_access(
    id INT NOT NULL PRIMARY KEY,
    company_id UUID REFERENCES company(id),
    user_agent VARCHAR(255),
    access_way VARCHAR(20),
    accessed_at TIMESTAMP,
    time_on_page INT,
    contacted BOOLEAN,
    contacted_at TIMESTAMP,
    contacted_from VARCHAR(20),
    created_at TIMESTAMP
);