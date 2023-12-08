CREATE TABLE IF NOT EXISTS transferHistory(
    id SERIAL PRIMARY KEY,
    debitTransactionId INT REFERENCES transaction(id),
    creditTransactionId INT REFERENCES transaction(id),
    amount DOUBLE PRECISION NOT NULL,
    transferDate TIMESTAMP NOT NULL,
    CONSTRAINT validation CHECK ( debitTransactionId IS NOT NULL AND creditTransactionId IS NOT NULL ),
    CONSTRAINT unicity UNIQUE (debitTransactionId, creditTransactionId, transferDate)
);

ALTER SEQUENCE transferHistory_id_seq RESTART WITH 1;

INSERT INTO TransferHistory (debitTransactionId, creditTransactionId, transferDate) VALUES
    (2, 1, '2023-01-02T12:00:00'),
    (1, 2, '2023-12-02 14:02:36'),
    (3, 1, '2023-01-03T15:30:00');

