package dev.lysmux.fcgi.param;

import java.util.ArrayList;
import java.util.List;

public class ArrayParameter<T> implements Parameter<List<Parameter<T>>> {
    private final List<Parameter<T>> values;

    public ArrayParameter(List<Parameter<T>> values) {
        this.values = new ArrayList<>(values);
    }

    public ArrayParameter() {
        this.values = new ArrayList<>();
    }

    @Override
    public List<Parameter<T>> get() {
        return values;
    }

    public void add(Parameter<T> value) {
        values.add(value);
    }

    public void addAll(List<Parameter<T>> values) {
        this.values.addAll(values);
    }

    public void addAll(ArrayParameter<T> values) {
        this.values.addAll(values.get());
    }

    public String toString() {
        return values.toString();
    }
}
