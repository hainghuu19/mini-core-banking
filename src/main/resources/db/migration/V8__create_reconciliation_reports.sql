CREATE TABLE reconciliation_reports (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    report_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    total_accounts_checked INT NOT NULL DEFAULT 0,
    total_transactions_checked INT NOT NULL DEFAULT 0,
    mismatches_found INT NOT NULL DEFAULT 0,
    debit_sum DECIMAL(18,2),
    credit_sum DECIMAL(18,2),
    detail JSONB,
    started_at TIMESTAMPTZ NOT NULL,
    completed_at TIMESTAMPTZ,
    CONSTRAINT chk_reconciliation_reports_status CHECK (status IN ('RUNNING', 'PASSED', 'FAILED', 'PARTIAL')),
    CONSTRAINT chk_reconciliation_reports_accounts_non_negative CHECK (total_accounts_checked >= 0),
    CONSTRAINT chk_reconciliation_reports_transactions_non_negative CHECK (total_transactions_checked >= 0),
    CONSTRAINT chk_reconciliation_reports_mismatches_non_negative CHECK (mismatches_found >= 0),
    CONSTRAINT chk_reconciliation_reports_debit_sum_non_negative CHECK (debit_sum IS NULL OR debit_sum >= 0),
    CONSTRAINT chk_reconciliation_reports_credit_sum_non_negative CHECK (credit_sum IS NULL OR credit_sum >= 0),
    CONSTRAINT chk_reconciliation_reports_completed_at CHECK (completed_at IS NULL OR completed_at >= started_at)
);

CREATE UNIQUE INDEX uq_reconciliation_reports_report_date
    ON reconciliation_reports(report_date);
