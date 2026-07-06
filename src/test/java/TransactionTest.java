import org.example.model.Transaction;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    @Test
    public void testCreateTransaction() {
        Transaction transaction = createTestTransaction();
        assertNotNull(transaction);
        assertEquals(Transaction.TransactionType.CREDIT, transaction.getType());
    }

    @Test
    public void testTransactionType() {
        Transaction debit = new Transaction(1, "Debit", 100.0, new Date(), Transaction.TransactionType.DEBIT, 1);
        assertEquals(Transaction.TransactionType.DEBIT, debit.getType());
    }

    @Test
    public void testTransactionValidation() {
        Transaction transaction = createTestTransaction();
        transaction.setCategory(0);
        assertThrows(IllegalArgumentException.class, () -> validateTransaction(transaction));
    }

    private void validateTransaction(Transaction transaction) {
        if (transaction.getCategory() == 0) {
            throw new IllegalArgumentException("The category is required for a transaction !");
        }
    }

    private List<Transaction> createTestTransactions() {
        return List.of(
                new Transaction(1, "Transaction1", 100.0, new Date(), Transaction.TransactionType.DEBIT, 1),
                new Transaction(2, "Transaction2", 200.0, new Date(), Transaction.TransactionType.CREDIT, 2),
                new Transaction(3, "Transaction3", 50.0, new Date(), Transaction.TransactionType.DEBIT, 1)
        );
    }

    private Transaction createTestTransaction() {
        return new Transaction(4, "TestTransaction", 75.0, new Date(), Transaction.TransactionType.CREDIT, 2);
    }
}
