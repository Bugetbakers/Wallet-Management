CREATE TABLE IF NOT EXISTS account(
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    balance NUMERIC(15,2) NOT NULL,
    transactionId INT REFERENCES transaction(id),
    currency INT REFERENCES currency(id),
    type VARCHAR(100) CHECK (type IN ('Bank', 'Cash', 'MobileMoney')) NOT NULL,
    current_date_time timestamp default now()
    );

ALTER SEQUENCE account_id_seq RESTART WITH 1;

INSERT INTO account (name, balance, transactionId, currency, type) VALUES
   ('John Doe', 1500.00, 2, 1, 'Cash'),
   ('Alice Smith', 2000.00, 3, 2, 'MobileMoney'),
   ('Bob Johnson', 500.50, 1, 3, 'Bank');
