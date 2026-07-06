import org.example.dao.CurrencyDAO;
import org.example.model.Currency;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CurrencyTest {
    private static Connection connection;
    
    @Test
    public void testFindAll() {
        if (connection != null) {
            CurrencyDAO currencyDAO = new CurrencyDAO(connection);
            List<Currency> currencies = currencyDAO.findAll();
            assertNotNull(currencies);
        }
    }

    @Test
    public void testSaveAll() {
        if (connection != null) {
            CurrencyDAO currencyDAO = new CurrencyDAO(connection);
            List<Currency> currenciesToSave = createTestCurrencies();
            List<Currency> savedCurrencies = currencyDAO.saveAll(currenciesToSave);
            assertNotNull(savedCurrencies);
        }
    }

    @Test
    public void testSave() {
        if (connection != null) {
            CurrencyDAO currencyDAO = new CurrencyDAO(connection);
            Currency currencyToSave = createTestCurrency();
            Currency savedCurrency = currencyDAO.save(currencyToSave);
            assertNotNull(savedCurrency);
        }
    }

    @Test
    public void testDelete() {
        if (connection != null) {
            CurrencyDAO currencyDAO = new CurrencyDAO(connection);
            Currency currencyToDelete = createTestCurrency();
            Currency deletedCurrency = currencyDAO.delete(currencyToDelete);
            assertNotNull(deletedCurrency);
        }
    }

    private List<Currency> createTestCurrencies() {
        Currency currency1 = new Currency();
        currency1.setCode("USD");
        currency1.setName("US Dollar");
        
        Currency currency2 = new Currency();
        currency2.setCode("EUR");
        currency2.setName("Euro");
        
        Currency currency3 = new Currency();
        currency3.setCode("GBP");
        currency3.setName("British Pound");
        
        return List.of(currency1, currency2, currency3);
    }

    private Currency createTestCurrency() {
        Currency currency = new Currency();
        currency.setCode("JPY");
        currency.setName("Japanese Yen");
        return currency;
    }
}
