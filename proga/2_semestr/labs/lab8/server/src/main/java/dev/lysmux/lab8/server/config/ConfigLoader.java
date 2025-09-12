package dev.lysmux.lab8.server.config;

import dev.lysmux.lab8.server.config.parser.ParserRegistry;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ConfigLoader<T extends Record> {
    private final Class<T> configClass;
    private final Map<String, String> configMap = new HashMap<>();

    public ConfigLoader<T> loadEnv() {
        updateConfigMap(System.getenv());
        return this;
    }

    private void updateConfigMap(Map<String, String> tree) {
        this.configMap.putAll(tree);
    }

    public T read(String nestedSeparator) {
        return makeRecord(configClass, "",nestedSeparator);
    }

    private <R extends Record> R makeRecord(Class<R> clazz, String prefix, String nestedSeparator) {
        var constructor = clazz.getDeclaredConstructors()[0];
        var params = constructor.getParameters();
        Object[] args = new Object[params.length];

        for (int i = 0; i < params.length; i++) {
            var param = params[i];
            var paramType = param.getType();
            var paramName = prefix + param.getName().toUpperCase();

            args[i] = paramType.isRecord()
                    ? makeRecord(paramType.asSubclass(Record.class), paramName + nestedSeparator, nestedSeparator)
                    : ParserRegistry.parse(configMap.get(paramName), param.getType());
        }

        try {
            return clazz.cast(constructor.newInstance(args));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to create record instance of: " + clazz.getName(), e);
        }
    }
}
