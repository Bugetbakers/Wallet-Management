CREATE TABLE IF NOT EXISTS CurrencyValue (
    id SERIAL PRIMARY KEY,
    baseCurrencyId INT NOT NULL,
    targetCurrencyId INT NOT NULL,
    value DOUBLE PRECISION NOT NULL,
    date DATE NOT NULL
);

ALTER SEQUENCE currencyValue_id_seq RESTART WITH 1;


INSERT INTO CurrencyValue (baseCurrencyId, targetCurrencyId, value, date) VALUES (1, 2, 4500, '2023-12-05');
INSERT INTO CurrencyValue (baseCurrencyId, targetCurrencyId, value, date) VALUES (1, 2, 4600, '2023-12-06');


