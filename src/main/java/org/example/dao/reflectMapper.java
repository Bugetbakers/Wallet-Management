package org.example.dao;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class reflectMapper<T> {
    public T map(ResultSet resultSet, Class<T> objectClass) {
        try {
            T object = objectClass.getDeclaredConstructor().newInstance();

            Field[] fields = objectClass.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                String fieldName = toSnakeCase(field.getName());
                Object value = resultSet.getObject(fieldName);

                if (value != null) {
                    if (field.getType().isEnum()) {
                        value = Enum.valueOf((Class<Enum>) field.getType(), value.toString());
                    } else if (field.getType() == LocalDateTime.class && value instanceof Timestamp) {
                        value = ((Timestamp) value).toLocalDateTime();
                    } else if (field.getType() == LocalDateTime.class && value instanceof java.sql.Date) {
                        value = ((java.sql.Date) value).toLocalDate().atStartOfDay();
                    }
                }

                field.set(object, value);
            }
            return object;
        } catch (SQLException | ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private String toSnakeCase(String camelCase) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < camelCase.length(); i++) {
            char c = camelCase.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) result.append('_');
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
