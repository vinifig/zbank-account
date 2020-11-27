CREATE TABLE "transaction"(
    id BIGSERIAL PRIMARY KEY,
    origin_account_id bigint,
    destiny_account_id bigint,
    kind VARCHAR(10),
    amount DOUBLE PRECISION,
    extra_amount DOUBLE PRECISION,
    created_at TIMESTAMP WITH TIME ZONE,

    FOREIGN KEY (origin_account_id) REFERENCES account (id),
    FOREIGN KEY (destiny_account_id) REFERENCES account (id)
);

CREATE INDEX transaction_origin_account_id_idx ON "transaction"(origin_account_id);
CREATE INDEX transaction_destiny_account_id_idx ON "transaction"(destiny_account_id);
