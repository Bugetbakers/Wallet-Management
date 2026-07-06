CREATE TABLE IF NOT EXISTS TransferHistory (
    id SERIAL PRIMARY KEY,
    debitTransactionId INT REFERENCES transactions(id),
    creditTransactionId INT REFERENCES transactions(id),
    transferDate TIMESTAMP NOT NULL,
    CONSTRAINT validation CHECK ( debitTransactionId IS NOT NULL AND creditTransactionId IS NOT NULL ),
    CONSTRAINT unicity UNIQUE (debitTransactionId, creditTransactionId, transferDate)
);

ALTER SEQUENCE IF EXISTS transferhistory_id_seq RESTART WITH 1;

INSERT INTO TransferHistory (debitTransactionId, creditTransactionId, transferDate) VALUES
    (2, 1, '2023-01-02 12:00:00'),
    (1, 2, '2023-12-02 14:02:36'),
    (3, 1, '2023-01-03 15:30:00');
