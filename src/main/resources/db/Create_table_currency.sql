CREATE TABLE IF NOT EXISTS currency(
    id SERIAL PRIMARY KEY,
    code VARCHAR(5) NOT NULL,
    name VARCHAR(20) NOT NULL,
    country VARCHAR(100) NOT NULL
);

ALTER SEQUENCE currency_id_seq RESTART WITH 1;

INSERT INTO currency (code, name, country) VALUES
    ('EUR', 'Euro', 'FRANCE'),
    ('GBP', 'British Pound','LONDON');
INSERT INTO currency (code, name, country)
SELECT 'JPY', 'Japanese Yen', 'JAPAN'
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE code = 'JPY');