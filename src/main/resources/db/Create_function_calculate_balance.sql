CREATE OR REPLACE FUNCTION calculate_balance(
    p_account_id INT,
    p_start_date TIMESTAMP,
    p_end_date TIMESTAMP
)
RETURNS DECIMAL(15, 2) AS $$
DECLARE
    v_total DECIMAL(15, 2);
BEGIN
    SELECT COALESCE(SUM(
        CASE
            WHEN transactionType = 'CREDIT' THEN amount
            WHEN transactionType = 'DEBIT' THEN -amount
            ELSE 0
        END
    ), 0.00) INTO v_total
    FROM transactions
    WHERE id_account = p_account_id
      AND date >= p_start_date
      AND date <= p_end_date;

    RETURN v_total;
END;
$$ LANGUAGE plpgsql;

-- Exemple d'appel :
-- SELECT calculate_balance(1, '2023-01-01 00:00:00', '2023-12-31 23:59:59') AS balance;
