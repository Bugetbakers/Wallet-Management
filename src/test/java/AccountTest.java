import org.example.dao.AccountDAO;
import org.example.model.Account;
import org.example.model.Currency;
import org.example.model.Transaction;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountTest {
    private static Connection connection;
    
    @Test
    public void testFindAll() {
        if (connection != null) {
            AccountDAO accountDAO = new AccountDAO(connection);
            List<Account> accounts = accountDAO.findAll();
            assertNotNull(accounts);
        }
    }

    @Test
    public void testFindById() {
        if (connection != null) {
            AccountDAO accountDAO = new AccountDAO(connection);
            int accountId = 1;
            Account account = accountDAO.findById(accountId);
            assertNotNull(account);
        }
    }

    @Test
    public void testGetTransactionsForAccount() {
        if (connection != null) {
            AccountDAO accountDAO = new AccountDAO(connection);
            int accountId = 1;
            List<Transaction> transactions = accountDAO.getTransactionsForAccount(accountId);
            assertNotNull(transactions);
        }
    }

    @Test
    public void testSaveAll() {
        if (connection != null) {
            AccountDAO accountDAO = new AccountDAO(connection);
            List<Account> accountsToSave = createTestAccounts();
            List<Account> savedAccounts = accountDAO.saveAll(accountsToSave);
            assertNotNull(savedAccounts);
        }
    }

    @Test
    public void testSave() {
        if (connection != null) {
            AccountDAO accountDAO = new AccountDAO(connection);
            Account accountToSave = createTestAccount();
            Account savedAccount = accountDAO.save(accountToSave);
            assertNotNull(savedAccount);
        }
    }

    @Test
    public void testDelete() {
        if (connection != null) {
            AccountDAO accountDAO = new AccountDAO(connection);
            Account accountToDelete = createTestAccount();
            Account deletedAccount = accountDAO.delete(accountToDelete);
            assertNotNull(deletedAccount);
        }
    }
    
    private List<Account> createTestAccounts() {
        List<Account> testAccounts = new ArrayList<>();

        Account account1 = new Account();
        account1.setName("Account1");
        account1.setBalance(1000.00);
        account1.setCurrency(new Currency(1, "USD", "US Dollar"));
        account1.setType(Account.AccountType.Bank);
        testAccounts.add(account1);

        Account account2 = new Account();
        account2.setName("Account2");
        account2.setBalance(500.00);
        account2.setCurrency(new Currency(2, "EUR", "Euro"));
        account2.setType(Account.AccountType.Cash);
        testAccounts.add(account2);

        return testAccounts;
    }

    private Account createTestAccount() {
        Account account = new Account();
        account.setName("TestAccount");
        account.setBalance(200.00);
        account.setCurrency(new Currency(3, "GBP", "British Pound"));
        account.setType(Account.AccountType.MobileMoney);
        return account;
    }
}
