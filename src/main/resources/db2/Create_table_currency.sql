CREATE TABLE IF NOT EXISTS currency(
    id SERIAL PRIMARY KEY,
    code VARCHAR(5) NOT NULL,
    name VARCHAR(20) NOT NULL
);

ALTER SEQUENCE currency_id_seq RESTART WITH 1;

INSERT INTO currency (code, name) VALUES
    ('EUR', 'Euro'),
    ('GBP', 'British Pound');
INSERT INTO currency (code, name)
SELECT 'JPY', 'Japanese Yen'
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE code = 'JPY');