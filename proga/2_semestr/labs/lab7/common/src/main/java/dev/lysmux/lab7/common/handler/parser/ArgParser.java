package dev.lysmux.lab7.common.handler.parser;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Class that helps convert {@code input} to needed {@code type}
 *
 * @since 1.0
 */
public class ArgParser {
    private final static Map<Class<?>, Function<String, ?>> converters = new HashMap<>();

    public static <T> void registerConverter(Class<T> type, Function<String, T> converter) {
        converters.put(type, converter);
    }

    /**
     * Checks if type can be converted
     *
     * @param type type for conversion
     * @return {@code true} if type can be converted else {@code false}
     */
    public static boolean canParse(Class<?> type) {
        return getArgType(type) != ArgType.UNKNOWN;
    }

    /**
     * Gets type of argument
     *
     * @param type type for conversion
     * @return type of argument
     */
    public static ArgType getArgType(Class<?> type) {
        if (type.isEnum()) return ArgType.ENUM;
        if (Date.class.equals(type)) return ArgType.DATE;
        if (converters.containsKey(type)) return ArgType.SIMPLE;
        return ArgType.UNKNOWN;
    }

    /**
     * Convert input to {@link Date}
     *
     * @param input  input for conversion
     * @param format date format
     * @return converted date
     */
    public static Date parseDate(String input, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate parsedDate = LocalDate.parse(input, formatter);
        return Date.from(parsedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Parses {@code input} to {@code type}
     *
     * @param input input to conversion
     * @param type  needed type
     * @param <T>   type of needed data type
     * @return converted input
     * @throws CouldNotParseArgException if input could be converted
     */
    public static <T> T parseArg(String input, Class<T> type) throws CouldNotParseArgException {
        ArgType argType = getArgType(type);

        try {
            return switch (argType) {
                case SIMPLE -> {
                    Function<String, ?> converter = converters.get(type);

                    @SuppressWarnings("unchecked")
                    T result = (T) converter.apply(input);
                    yield result;
                }
                case ENUM -> type.cast(Enum.valueOf(type.asSubclass(Enum.class), input.toUpperCase()));
                case DATE -> type.cast(parseDate(input, "dd.MM.yyyy"));
                case UNKNOWN ->
                        throw new RuntimeException("No converter found for type `%s`".formatted(type.getSimpleName()));
            };
        } catch (Exception exc) {
            throw new CouldNotParseArgException(input, type.getSimpleName());
        }
    }

    static {
        registerConverter(boolean.class, Boolean::parseBoolean);
        registerConverter(int.class, Integer::parseInt);
        registerConverter(long.class, Long::parseLong);
        registerConverter(float.class, Float::parseFloat);
        registerConverter(double.class, Double::parseDouble);

        registerConverter(Boolean.class, Boolean::parseBoolean);
        registerConverter(Integer.class, Integer::parseInt);
        registerConverter(Long.class, Long::parseLong);
        registerConverter(Float.class, Float::parseFloat);
        registerConverter(Double.class, Double::parseDouble);
        registerConverter(String.class, String::valueOf);
        registerConverter(Path.class, Path::of);
    }
}
