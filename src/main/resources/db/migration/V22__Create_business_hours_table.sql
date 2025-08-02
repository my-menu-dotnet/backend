-- Criação da tabela business_hours para gerenciar horários de funcionamento
CREATE TABLE business_hours (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL,
    day_of_week VARCHAR(10) NOT NULL,
    opening_time TIME,
    closing_time TIME,
    is_closed BOOLEAN NOT NULL DEFAULT false,
    CONSTRAINT fk_business_hours_company FOREIGN KEY (company_id) REFERENCES company(id) ON DELETE CASCADE,
    CONSTRAINT uk_business_hours_company_day UNIQUE (company_id, day_of_week),
    CONSTRAINT chk_business_hours_day_of_week CHECK (day_of_week IN ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY')),
    CONSTRAINT chk_business_hours_times CHECK (
        (is_closed = true AND opening_time IS NULL AND closing_time IS NULL) OR
        (is_closed = false AND opening_time IS NOT NULL AND closing_time IS NOT NULL AND opening_time < closing_time)
    )
);

-- Índice para melhorar performance nas consultas
CREATE INDEX idx_business_hours_company_day ON business_hours(company_id, day_of_week);
