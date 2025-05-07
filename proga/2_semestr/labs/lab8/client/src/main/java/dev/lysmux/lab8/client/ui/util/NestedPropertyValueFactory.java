package dev.lysmux.lab8.client.ui.util;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.lang.reflect.Field;

public class NestedPropertyValueFactory<S, T> implements Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> {

    private final String property;

    public NestedPropertyValueFactory(String property) {
        this.property = property;
    }

    @Override
    public ObservableValue<T> call(TableColumn.CellDataFeatures<S, T> cellDataFeatures) {
        S item = cellDataFeatures.getValue();
        try {
            Object value = resolveNestedProperty(item, property);
            return new ReadOnlyObjectWrapper<>((T) value);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object resolveNestedProperty(Object obj, String propertyPath) throws Exception {
        String[] properties = propertyPath.split("\\.");
        Object value = obj;
        for (String prop : properties) {
            if (value == null) return null;

            Field field = value.getClass().getDeclaredField(prop);
            field.setAccessible(true);
            value = field.get(value);
        }
        return value;
    }
}
