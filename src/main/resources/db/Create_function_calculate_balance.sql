CREATE FUNCTION calculate_balance(
    account_id INT,
    start_date_time DATETIME,
    end_date_time DATETIME
)
    RETURNS DECIMAL(10, 2)
BEGIN
    DECLARE total DECIMAL(10, 2);

SELECT SUM(
               CASE
                   WHEN amount >= 0 THEN amount
                   ELSE 0
                   END
       ) INTO total
FROM transactions
WHERE account_id = account_id
  AND date >= start_date_time
  AND date <= end_date_time;

SELECT SUM(
               CASE
                   WHEN amount < 0 THEN amount
                   ELSE 0
                   END
       ) INTO total
FROM transactions
WHERE account_id = account_id
  AND date >= start_date_time
  AND date <= end_date_time;

RETURN total;
END;

SELECT calculate_balance(1, '2023-01-01 00:00:00', '2023-12-31 23:59:59') AS balance;
