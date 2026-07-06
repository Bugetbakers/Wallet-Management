package org.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class CrudOperationReflect<T> implements CrudOperation<T> {
    protected Connection connection;

    public CrudOperationReflect(Connection connection){
        this.connection = connection;
    }
    protected abstract String getTableName();
    protected abstract String getInsertColumns();
    protected abstract String getInsertValues();
    protected abstract T mapResultSetToObject(ResultSet resultSet) throws SQLException;
    protected abstract void setPreparedStatementParameters(PreparedStatement preparedStatement, T object) throws SQLException;

    @Override
    public List<T> findAll() throws SQLException {
        List<T> result = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                result.add(mapResultSetToObject(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public T findById(int id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToObject(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public T save(T object){
        String sql = "INSERT INTO " + getTableName() + " " + getInsertColumns() + " VALUES " + getInsertValues();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setPreparedStatementParameters(preparedStatement, object);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    @Override
    public List<T> saveAll(List<T> toSave) {
        List<T> savedList = new ArrayList<>();
        for (T object : toSave) {
            savedList.add(save(object));
        }
        return savedList;
    }

    @Override
    public T delete(T toDelete) {
        try {
            java.lang.reflect.Field idField = toDelete.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            int id = (int) idField.get(toDelete);
            String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);
                int rows = preparedStatement.executeUpdate();
                if (rows > 0) {
                    return toDelete;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
