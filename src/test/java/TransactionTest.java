import org.example.dao.TransactionDAO;
import org.example.model.Transaction;
import org.junit.Test;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransactionTest {
    private static Connection connection;
    @Test
    public void testFindAll() {
        TransactionDAO transactionDAO = new TransactionDAO(connection);
        List<Transaction> transactions = transactionDAO.findAll();
        assertNotNull(transactions);
    }

    @Test
    public void testInsertTransaction() {
        TransactionDAO transactionDAO = new TransactionDAO(connection);
        Transaction testTransaction = createTestTransaction();
        assertThrows(IllegalArgumentException.class, () -> transactionDAO.insertTransaction(testTransaction));
    }

    @Test
    public void testSaveAll() {
        TransactionDAO transactionDAO = new TransactionDAO(connection);
        List<Transaction> transactionsToSave = createTestTransactions();
        List<Transaction> savedTransactions = transactionDAO.saveAll(transactionsToSave);
        assertNotNull(savedTransactions);
    }

    @Test
    public void testSave() {
        TransactionDAO transactionDAO = new TransactionDAO(connection);
        Transaction testTransaction = createTestTransaction();
        Transaction savedTransaction = transactionDAO.save(testTransaction);
        assertNotNull(savedTransaction);
    }

    @Test
    public void testDelete() {
        TransactionDAO transactionDAO = new TransactionDAO(connection);
        Transaction testTransaction = createTestTransaction();
        Transaction deletedTransaction = transactionDAO.delete(testTransaction);
        assertNotNull(deletedTransaction);
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
