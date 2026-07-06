INSERT INTO transactionCategory (name, transaction_type) VALUES
      ('Food and Drink', 'Output'),
      ('Transportation', 'Output'),
      ('Shopping', 'Output'),
      ('Health and Fitness', 'Output'),
      ('Travel', 'Output'),
      ('Education', 'Output'),
      ('Personal Care', 'Output'),
      ('Gifts and Donations', 'Input and Output'),
      ('Home and Rent', 'Output'),
      ('Investments', 'Input and Output'),
      ('Income', 'Input')
ON CONFLICT (name) DO NOTHING;