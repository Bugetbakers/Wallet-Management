package org.example.Repository;


import java.sql.SQLException;
import java.util.List;

public interface CrudOperation<T>{
    List<T> findAll() throws SQLException;
    List<T> saveAll(List<T> toSave);
    T save(T toSave);
    T delete(T toDelete);
}
