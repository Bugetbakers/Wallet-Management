package org.example.dao;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class reflectMapper<T> {
    public T map(ResultSet resultSet, Class<T> objectClass) {
        try {
            T object = objectClass.getDeclaredConstructor().newInstance();

            Field[] fields = objectClass.getDeclaredFields();

            for (Field field : fields) {

                field.setAccessible(true);

                String fieldName = field.getName();

                Object value = resultSet.getObject(fieldName);

                field.set(object, value);
            }
            return object;
        } catch (SQLException | ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
