CREATE TABLE IF NOT EXISTS transactions(
    id SERIAL PRIMARY KEY,
    label TEXT,
    amount DOUBLE PRECISION,
    date TIMESTAMP default current_timestamp,
    transactionType VARCHAR(20) DEFAULT 'DEBIT' CHECK ( transactionType IN ('DEBIT', 'CREDIT')),
    id_account int REFERENCES account(id),
    id_category int REFERENCES transactionCategory(id) NOT NULL
);

ALTER SEQUENCE transactions_id_seq RESTART WITH 1;

INSERT INTO transactions (label, amount, date, transactionType)
VALUES
    ((SELECT id FROM account WHERE name = 'John Doe'), 'food', 10000.00, '2023-12-10 10:14:36', 'DEBIT');

UPDATE transactions
SET transactionType = (
    SELECT transaction_type
    FROM transactionCategory
    WHERE transactionCategory.id = transactions.category_id
);