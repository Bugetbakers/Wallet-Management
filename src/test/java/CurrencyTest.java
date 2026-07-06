import org.example.model.Currency;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CurrencyTest {

    @Test
    public void testCreateCurrency() {
        Currency currency = createTestCurrency();
        assertNotNull(currency);
        assertEquals("JPY", currency.getCode());
    }

    @Test
    public void testCurrencyFields() {
        List<Currency> currencies = createTestCurrencies();
        assertEquals(3, currencies.size());
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
