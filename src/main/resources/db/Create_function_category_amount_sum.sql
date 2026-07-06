CREATE OR REPLACE FUNCTION GetCategoryAmounts(
    p_bank_account_id INT,
    p_start_date TIMESTAMP,
    p_end_date TIMESTAMP
)
RETURNS TABLE (
    Restaurant DOUBLE PRECISION,
    Salary DOUBLE PRECISION
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        COALESCE(SUM(CASE WHEN c.name = 'Restaurant' THEN t.amount ELSE 0 END), 0)::DOUBLE PRECISION AS Restaurant,
        COALESCE(SUM(CASE WHEN c.name = 'Salary' THEN t.amount ELSE 0 END), 0)::DOUBLE PRECISION AS Salary
    FROM
        transactions t
        LEFT JOIN transactionCategory c ON t.id_category = c.id
    WHERE
        t.id_account = p_bank_account_id
        AND t.date >= p_start_date
        AND t.date <= p_end_date;
END;
$$ LANGUAGE plpgsql;

-- Exemple d'appel :
-- SELECT * FROM GetCategoryAmounts(1, '2023-12-01 00:00:00', '2023-12-31 23:59:59');
