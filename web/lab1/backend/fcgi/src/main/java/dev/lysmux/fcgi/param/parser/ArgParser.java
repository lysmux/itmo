package dev.lysmux.fcgi.param.parser;

import dev.lysmux.fcgi.param.parser.exception.ParseException;
import dev.lysmux.fcgi.param.parser.exception.ParserNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ArgParser {
    private final Map<Class<?>, Function<String, ?>> parsers = new HashMap<>();

    public <T> Function<String, T> getParser(Class<T> clazz) {
        return (Function<String, T>) parsers.get(clazz);
    }

    public <T> void registerParser(Class<T> clazz, Function<String, T> parser) {
        parsers.put(clazz, parser);
    }

    public <T> T parse(String arg, Class<T> type) {
        Function<String, T> parser = getParser(type);
        if (parser == null) {
            throw new ParserNotFoundException(type);
        }

        try {
            return parser.apply(arg);
        } catch (Exception e) {
            throw new ParseException(arg, type);
        }
    }

    {
        registerParser(String.class, s -> s);
        registerParser(Integer.class, Integer::parseInt);
        registerParser(Double.class, Double::parseDouble);
        registerParser(Float.class, Float::parseFloat);
        registerParser(Long.class, Long::parseLong);
        registerParser(Boolean.class, Boolean::parseBoolean);
        registerParser(Integer.TYPE, Integer::parseInt);
        registerParser(Double.TYPE, Double::parseDouble);
        registerParser(Float.TYPE, Float::parseFloat);
        registerParser(Long.TYPE, Long::parseLong);
        registerParser(Boolean.TYPE, Boolean::parseBoolean);
    }
}
