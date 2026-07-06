import org.example.dao.TransactionDAO;
import org.example.model.Transaction;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TransactionTest {
    private static Connection connection;
    
    @Test
    public void testFindAll() {
        if (connection != null) {
            TransactionDAO transactionDAO = new TransactionDAO(connection);
            List<Transaction> transactions = transactionDAO.findAll();
            assertNotNull(transactions);
        }
    }

    @Test
    public void testInsertTransaction() {
        if (connection != null) {
            TransactionDAO transactionDAO = new TransactionDAO(connection);
            Transaction testTransaction = createTestTransaction();
            transactionDAO.insertTransaction(testTransaction);
            assertNotNull(testTransaction.getId());
        }
    }

    @Test
    public void testSaveAll() {
        if (connection != null) {
            TransactionDAO transactionDAO = new TransactionDAO(connection);
            List<Transaction> transactionsToSave = createTestTransactions();
            List<Transaction> savedTransactions = transactionDAO.saveAll(transactionsToSave);
            assertNotNull(savedTransactions);
        }
    }

    @Test
    public void testSave() {
        if (connection != null) {
            TransactionDAO transactionDAO = new TransactionDAO(connection);
            Transaction testTransaction = createTestTransaction();
            Transaction savedTransaction = transactionDAO.save(testTransaction);
            assertNotNull(savedTransaction);
        }
    }

    @Test
    public void testDelete() {
        if (connection != null) {
            TransactionDAO transactionDAO = new TransactionDAO(connection);
            Transaction testTransaction = createTestTransaction();
            Transaction deletedTransaction = transactionDAO.delete(testTransaction);
            assertNotNull(deletedTransaction);
        }
    }

    private List<Transaction> createTestTransactions() {
        Transaction transaction1 = new Transaction();
        transaction1.setLabel("Transaction1");
        transaction1.setAmount(100.0);
        transaction1.setDate(new Date());
        transaction1.setType(Transaction.TransactionType.DEBIT);
        transaction1.setCategory(1);
        
        Transaction transaction2 = new Transaction();
        transaction2.setLabel("Transaction2");
        transaction2.setAmount(200.0);
        transaction2.setDate(new Date());
        transaction2.setType(Transaction.TransactionType.CREDIT);
        transaction2.setCategory(2);
        
        Transaction transaction3 = new Transaction();
        transaction3.setLabel("Transaction3");
        transaction3.setAmount(50.0);
        transaction3.setDate(new Date());
        transaction3.setType(Transaction.TransactionType.DEBIT);
        transaction3.setCategory(1);
        
        return List.of(transaction1, transaction2, transaction3);
    }

    private Transaction createTestTransaction() {
        Transaction transaction = new Transaction();
        transaction.setLabel("TestTransaction");
        transaction.setAmount(75.0);
        transaction.setDate(new Date());
        transaction.setType(Transaction.TransactionType.CREDIT);
        transaction.setCategory(2);
        return transaction;
    }
}
