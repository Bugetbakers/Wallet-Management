CREATE FUNCTION GetCategoryAmounts(
    @BankAccountId INT,
    @StartDate DATETIME,
    @EndDate DATETIME
)
    RETURNS TABLE
    AS
RETURN
(
  SELECT
    COALESCE(SUM(CASE WHEN t.Category = 'Restaurant' THEN t.Amount ELSE 0 END), 0) AS Restaurant,
    COALESCE(SUM(CASE WHEN t.Category = 'Salary' THEN t.Amount ELSE 0 END), 0) AS Salary
  FROM
    Transactions t
    LEFT JOIN Categories c ON t.CategoryId = c.CategoryId
  WHERE
    t.BankAccountId = @BankAccountId
    AND t.TransactionDate >= @StartDate
    AND t.TransactionDate <= @EndDate
)

SELECT * FROM GetCategoryAmounts(1, '2023-12-01', '2023-12-02')
