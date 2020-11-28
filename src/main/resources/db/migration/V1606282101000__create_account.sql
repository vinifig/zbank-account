CREATE TABLE account(
    id BIGSERIAL PRIMARY KEY,
    name TEXT,
    cpf TEXT,
    balance FLOAT(3),
    updated_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE
);

CREATE UNIQUE INDEX account_cpf_idx ON account(cpf);
