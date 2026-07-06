import org.example.dao.AccountDAO;
import org.example.model.Account;
import org.example.model.Currency;
import org.example.model.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountTest {

    @Test
    public void testCreateAccount() {
        Account account = createTestAccount();
        assertNotNull(account);
        assertNotNull(account.getName());
    }

    @Test
    public void testAccountType() {
        Account account = createTestAccount();
        assertNotNull(account.getType());
    }

    private List<Account> createTestAccounts() {
        List<Account> testAccounts = new ArrayList<>();

        testAccounts.add(new Account(1, "Account1", 1000.0, List.of(new Transaction()),
                new Currency(1, "USD", "US Dollar", "United States"), Account.AccountType.SAVINGS, LocalDateTime.now()));

        testAccounts.add(new Account(2, "Account2", 500.0, List.of(new Transaction()),
                new Currency(2, "EUR", "Euro", "Eurozone"), Account.AccountType.CHECKING, LocalDateTime.now()));

        return testAccounts;
    }

    private Account createTestAccount() {
        return new Account(3, "TestAccount", 200.0, List.of(new Transaction()),
                new Currency(3, "GBP", "British Pound", "United Kingdom"), Account.AccountType.SAVINGS, LocalDateTime.now());
    }
}
