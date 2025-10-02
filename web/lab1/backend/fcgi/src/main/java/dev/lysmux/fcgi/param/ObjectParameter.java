package dev.lysmux.fcgi.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectParameter implements MapParameter<Map<String, Parameter<?>>>{
    private final Map<String, Parameter<?>> values;

    public ObjectParameter(Map<String, Parameter<?>> values) {
        this.values = values;
    }

    public ObjectParameter() {
        this.values = new HashMap<>();
    }

    @Override
    public Map<String, Parameter<?>> get() {
        return values;
    }

    public Parameter<?> get(String key) {
        return values.get(key);
    }

    public void put(String key, Parameter<?> value) {
        values.compute(key, (k, param) -> {
            if (param == null) return value;
            if (param.isArray()) ((ArrayParameter) param).add(value);
            return new ArrayParameter(List.of(param, value));
        });
    }
}
