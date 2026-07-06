CREATE TABLE IF NOT EXISTS account(
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    balance NUMERIC(15,2) NOT NULL,
    transactionId INT, -- Will reference transactions(id) later to avoid circular dependency on insert
    currency INT REFERENCES currency(id),
    type VARCHAR(100) CHECK (type IN ('Bank', 'Cash', 'MobileMoney')) NOT NULL,
    current_date_time timestamp default now()
);

ALTER SEQUENCE account_id_seq RESTART WITH 1;

INSERT INTO account (id, name, balance, transactionId, currency, type) VALUES
   (1, 'John Doe', 1500.00, NULL, 1, 'Cash'),
   (2, 'Alice Smith', 2000.00, NULL, 2, 'MobileMoney'),
   (3, 'Bob Johnson', 500.50, NULL, 3, 'Bank');
