package dev.lysmux.lab7.server.config.parser;

import java.util.HashMap;
import java.util.Map;

public class ParserRegistry {
    private static final Map<Class<?>, Parser<?>> registry = new HashMap<>();

    private static Class<?> normalizePrimitiveType(Class<?> type) {
        if (!type.isPrimitive()) return type;
        if (type == int.class) return Integer.class;
        if (type == long.class) return Long.class;
        if (type == double.class) return Double.class;
        if (type == float.class) return Float.class;
        if (type == boolean.class) return Boolean.class;
        if (type == byte.class) return Byte.class;
        if (type == short.class) return Short.class;
        if (type == char.class) return Character.class;
        return type;
    }

    public static <T> void register(Class<T> clazz, Parser<T> parser) {
        registry.put(clazz, parser);
    }

    @SuppressWarnings("unchecked")
    public static <T> T parse(String input, Class<T> clazz) {
        Class<?> type = normalizePrimitiveType(clazz);
        Parser<T> parser = (Parser<T>) registry.get(type);
        if (parser == null) {
            throw new RuntimeException("No parser registered for " + clazz);
        }
        return parser.parse(input);
    }

    static {
        register(String.class, input -> input);
        register(Integer.class, input -> input != null ? Integer.parseInt(input) : 0);
        register(Double.class, input -> input != null ? Double.parseDouble(input) : 0d);
        register(Boolean.class, Boolean::parseBoolean);
    }
}
