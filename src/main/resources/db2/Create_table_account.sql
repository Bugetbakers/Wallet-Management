CREATE TABLE IF NOT EXISTS account(
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    balance NUMERIC(15,2) NOT NULL,
    currency int references currency(id),
    type VARCHAR(100) NOT NULL
);

ALTER SEQUENCE account_id_seq RESTART WITH 1;

INSERT INTO account (name, balance, currency, password) VALUES
    ('John Doe', 1500.00, 'USD', 'dqfkl,qv,KHLbkj'),
    ('Alice Smith', 2000.00, 'EUR', 'JKNKkNKJNN:;'),
    ('Bob Johnson', 500.50, 'GBP', 'jkjKJNSCESds3');

CREATE VIEW personnalAccount AS SELECT id, name, balance, currency FROM account;