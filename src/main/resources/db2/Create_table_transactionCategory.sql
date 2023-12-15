CREATE TABLE IF NOT EXISTS transactionCategory (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

ALTER SEQUENCE transactionCategory_id_seq RESTART WITH 1;

INSERT INTO transactionCategory (name) VALUES ('Salary') ON CONFLICT (name) DO NOTHING;
INSERT INTO transactionCategory (name) VALUES ('Restaurant') ON CONFLICT (name) DO NOTHING;
INSERT INTO transactionCategory (name) VALUES ('Loan') ON CONFLICT (name) DO NOTHING;
INSERT INTO transactionCategory (name) VALUES ('Groceries') ON CONFLICT (name) DO NOTHING;
INSERT INTO transactionCategory (name) VALUES ('Utilities') ON CONFLICT (name) DO NOTHING;
INSERT INTO transactionCategory (name) VALUES ('Entertainment') ON CONFLICT (name) DO NOTHING;


ALTER TABLE transactionCategory ADD COLUMN transaction_type VARCHAR(255);

UPDATE transactionCategory
    SET transaction_type = 'Input'
    WHERE name IN ('Salary');

UPDATE transactionCategory
    SET transaction_type = 'Output'
    WHERE name IN ('Restaurant');

UPDATE transactioncategory
    SET transaction_type = 'Input and Output'
    WHERE name IN ('Loan');

UPDATE transactionCategory
    SET transaction_type = 'Output'
    WHERE name IN ('Groceries', 'Utilities', 'Entertainment');

SELECT COALESCE(SUM(CASE WHEN t.amount > 0 THEN t.amount ELSE 0 END), 0) AS totalInput,  +
                     COALESCE(SUM(CASE WHEN t.amount < 0 THEN t.amount ELSE 0 END), 0) AS totalOutput
                     FROM transaction t
                     WHERE t.account_id = ? AND t.transaction_date BETWEEN ? AND ?;
