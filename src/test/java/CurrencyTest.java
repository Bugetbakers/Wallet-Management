import org.example.dao.CurrencyDAO;
import org.example.model.Currency;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CurrencyTest {
    private static Connection connection;
    @Test
    public void testFindAll() {
        CurrencyDAO currencyDAO = new CurrencyDAO(connection);
        List<Currency> currencies = currencyDAO.findAll();
        assertNotNull(currencies);
    }

    @Test
    public void testSaveAll() {
        CurrencyDAO currencyDAO = new CurrencyDAO(connection);
        List<Currency> currenciesToSave = createTestCurrencies();
        List<Currency> savedCurrencies = currencyDAO.saveAll(currenciesToSave);
        assertNotNull(savedCurrencies);
    }

    @Test
    public void testSave() {
        CurrencyDAO currencyDAO = new CurrencyDAO(connection);
        Currency currencyToSave = createTestCurrency();
        Currency savedCurrency = currencyDAO.save(currencyToSave);
        assertNotNull(savedCurrency);
    }

    @Test
    public void testDelete() {
        CurrencyDAO currencyDAO = new CurrencyDAO(connection);
        Currency currencyToDelete = createTestCurrency();
        Currency deletedCurrency = currencyDAO.delete(currencyToDelete);
        assertNotNull(deletedCurrency);
    }

    private List<Currency> createTestCurrencies() {
        return List.of(
                new Currency(1, "USD", "US Dollar", "United States"),
                new Currency(2, "EUR", "Euro", "Eurozone"),
                new Currency(3, "GBP", "British Pound", "United Kingdom")

        );
    }

    private Currency createTestCurrency() {
        return new Currency(4, "JPY", "Japanese Yen", "Japan");
    }
}
