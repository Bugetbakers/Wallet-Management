CREATE TABLE IF NOT EXISTS transactions(
    id SERIAL PRIMARY KEY,
    sender_account_id BIGINT,
    receiver_account_id BIGINT,
    amount DOUBLE PRECISION,
    currency VARCHAR(3),
    description VARCHAR(255)
);

ALTER SEQUENCE transactions_id_seq RESTART WITH 1;

INSERT INTO transactions (sender_account_id, receiver_account_id, amount, currency, description)
VALUES
    ((SELECT id FROM account WHERE name = 'John Doe'), 2, 100.00, 'USD', 'Paiement pour services');

INSERT INTO transactions (sender_account_id, receiver_account_id, amount, currency, description)
VALUES
    (2, 1, 50.50, (SELECT name FROM currency WHERE code = 'FMG'), 'Remboursement');

INSERT INTO transactions (sender_account_id, receiver_account_id, amount, currency, description)
VALUES
    (3, 1, 75.25, 'GBP', CONCAT('Achat en ligne - ', (SELECT name FROM account WHERE id = 3)));