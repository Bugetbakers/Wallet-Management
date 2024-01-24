package org.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class CrudOperationReflect<T> {
    private Connection connection;

    public CrudOperationReflect(Connection connection){
        this.connection = connection;
    }
    public List<T> findAll(){
        List<T> result = new ArrayList<>();
        try{
            String sql= "Select* from "+ getTableName();
            try{
                ResultSet resultSet= connection.createStatement().executeQuery(sql);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }catch (Exception e){
            throw  new RuntimeException(e);
        }
        return result;
    }
    public T save(T object){
        try{
            String sql = "Insert into " +getTableName() + " " + getInsertColumns() + " Values" + getInsertValues();
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                setPreparedStatementParameters(preparedStatement, object);
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return object;
    }
    protected abstract String getTableName();
    protected abstract String getInsertColumns();
    protected abstract String getInsertValues();
    protected abstract T mapResultSetToObject(ResultSet resultSet) throws SQLException, IllegalAccessException;
    protected  abstract T setPreparedStatementParameters(PreparedStatement preparedStatement, T object) throws SQLException;
}
