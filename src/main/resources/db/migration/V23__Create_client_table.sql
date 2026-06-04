-- Client (Customer) table — tenant-scoped. Each company has its own client base.
-- Clients are distinct from app_user: app_user is for authenticated admins/operators.
CREATE TABLE client
(
    id          UUID         NOT NULL,
    tenant_id   UUID         NOT NULL,
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255),
    phone       VARCHAR(255),
    cpf         VARCHAR(255),
    address_id  UUID,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_client PRIMARY KEY (id),
    CONSTRAINT fk_client_on_address FOREIGN KEY (address_id) REFERENCES address (id) ON DELETE SET NULL
);

-- Indexes for typeahead search and tenant scoping
CREATE INDEX idx_client_tenant ON client (tenant_id);
CREATE INDEX idx_client_name ON client (name);
CREATE INDEX idx_client_phone ON client (phone);
