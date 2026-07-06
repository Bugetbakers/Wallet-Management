CREATE TABLE IF NOT EXISTS transactions(
    id SERIAL PRIMARY KEY,
    label TEXT,
    amount DOUBLE PRECISION,
    date TIMESTAMP default current_timestamp,
    transactionType VARCHAR(20) DEFAULT 'DEBIT' CHECK ( transactionType IN ('DEBIT', 'CREDIT')),
    id_account int REFERENCES account(id),
    id_category int REFERENCES transactionCategory(id)
);

ALTER SEQUENCE transactions_id_seq RESTART WITH 1;

-- Add the foreign key constraint to account now that transactions table exists
ALTER TABLE account ADD CONSTRAINT fk_account_transaction FOREIGN KEY (transactionId) REFERENCES transactions(id);

-- Insert seed transactions
INSERT INTO transactions (id, label, amount, date, transactionType, id_account, id_category) VALUES
    (1, 'Salary', 500.50, '2023-12-01 09:00:00', 'CREDIT', 3, (SELECT id FROM transactionCategory WHERE name = 'Salary')),
    (2, 'Salary', 1500.00, '2023-12-01 09:30:00', 'CREDIT', 1, (SELECT id FROM transactionCategory WHERE name = 'Salary')),
    (3, 'Salary', 2000.00, '2023-12-01 10:00:00', 'CREDIT', 2, (SELECT id FROM transactionCategory WHERE name = 'Salary'))
ON CONFLICT (id) DO NOTHING;

-- Update account to point to transactions
UPDATE account SET transactionId = 1 WHERE id = 3;
UPDATE account SET transactionId = 2 WHERE id = 1;
UPDATE account SET transactionId = 3 WHERE id = 2;