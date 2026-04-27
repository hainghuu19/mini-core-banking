CREATE TABLE ledger_entries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_id UUID NOT NULL REFERENCES transactions(id),
    account_id UUID NOT NULL REFERENCES accounts(id),
    entry_type VARCHAR(10) NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    balance_before DECIMAL(18,2) NOT NULL,
    balance_after DECIMAL(18,2) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_ledger_entries_entry_type CHECK (entry_type IN ('DEBIT', 'CREDIT')),
    CONSTRAINT chk_ledger_entries_amount_positive CHECK (amount > 0),
    CONSTRAINT chk_ledger_entries_balance_before_non_negative CHECK (balance_before >= 0),
    CONSTRAINT chk_ledger_entries_balance_after_non_negative CHECK (balance_after >= 0),
    CONSTRAINT chk_ledger_entries_balance_flow CHECK (
        (entry_type = 'DEBIT' AND balance_after = balance_before - amount)
        OR (entry_type = 'CREDIT' AND balance_after = balance_before + amount)
    )
);

CREATE INDEX idx_ledger_entries_transaction_id ON ledger_entries(transaction_id);
CREATE INDEX idx_ledger_entries_account_id ON ledger_entries(account_id);

CREATE OR REPLACE FUNCTION prevent_append_only_modifications()
RETURNS trigger AS $$
BEGIN
    RAISE EXCEPTION 'Table % is append-only. % is not allowed.', TG_TABLE_NAME, TG_OP;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_ledger_entries_append_only
BEFORE UPDATE OR DELETE ON ledger_entries
FOR EACH ROW
EXECUTE FUNCTION prevent_append_only_modifications();
