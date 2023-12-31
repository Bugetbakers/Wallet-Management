CREATE TABLE IF NOT EXISTS ExchangeRate (
    id SERIAL PRIMARY KEY,
    SourceCurrency VARCHAR(255) NOT NULL,
    DestinationCurrency VARCHAR(255) NOT NULL,
    Value DOUBLE PRECISION NOT NULL,
    ChangeDateTime DATE NOT NULL
);

ALTER SEQUENCE ExchangeRate_id_seq RESTART WITH 1;
