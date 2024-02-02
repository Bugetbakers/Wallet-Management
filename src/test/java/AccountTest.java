import org.example.dao.AccountDAO;
import org.example.model.Account;
import org.example.model.Currency;
import org.example.model.Transaction;
import org.junit.Test;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountTest {
    private static Connection connection;
    @Test
    public void testFindAll() {
        AccountDAO accountDAO = new AccountDAO(connection);
        List<Account> accounts = accountDAO.findAll();
        assertNotNull(accounts);
    }

    @Test
    public void testFindById() {
        AccountDAO accountDAO = new AccountDAO(connection);
        int accountId = 1;
        Account account = accountDAO.findById(accountId);
        assertNotNull(account);
    }

    @Test
    public void testGetTransactionsForAccount() {
        AccountDAO accountDAO = new AccountDAO(connection);
        int accountId = 1; // Replace with an existing account ID in your database
        List<Transaction> transactions = accountDAO.getTransactionsForAccount(accountId);
        assertNotNull(transactions);
    }

    @Test
    public void testSaveAll() {
        AccountDAO accountDAO = new AccountDAO(connection);
        List<Account> accountsToSave = createTestAccounts();
        List<Account> savedAccounts = accountDAO.saveAll(accountsToSave);
        assertNotNull(savedAccounts);
    }

    @Test
    public void testSave() {
        AccountDAO accountDAO = new AccountDAO(connection);
        Account accountToSave = createTestAccount();
        Account savedAccount = accountDAO.save(accountToSave);
        assertNotNull(savedAccount);
    }

    @Test
    public void testDelete() {
        AccountDAO accountDAO = new AccountDAO(connection);
        Account accountToDelete = createTestAccount();
        Account deletedAccount = accountDAO.delete(accountToDelete);
        assertNotNull(deletedAccount);

    }
    private List<Account> createTestAccounts() {
        List<Account> testAccounts = new ArrayList<>();

        testAccounts.add(new Account(1, "Account1", 1000.0, List.of(new Transaction()),
                new Currency(1, "USD", "US Dollar"), Account.AccountTypegit .SAVINGS, LocalDateTime.now()));

        testAccounts.add(new Account(2, "Account2", 500.0, List.of(new Transaction()),
                new Currency(2, "EUR", "Euro"), Account.AccountType.CHECKING, LocalDateTime.now()));

        return testAccounts;
    }

    private Account createTestAccount() {
        return new Account(3, "TestAccount", 200.0, List.of(new Transaction()),
                new Currency(3, "GBP", "British Pound"), Account.AccountType.SAVINGS, LocalDateTime.now());
    }
}
