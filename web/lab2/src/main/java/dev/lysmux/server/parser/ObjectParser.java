package dev.lysmux.server.parser;

import dev.lysmux.server.parser.arg.ArgParser;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Set;

@Slf4j
public class ObjectParser {
    public static <T> T parse(Map<String, String[]> parameters, Class<T> clazz) {
        T object = parseObject(parameters, clazz);
        validate(object);

        return object;
    }

    private static <T> T parseObject(Map<String, String[]> parameters, Class<T> clazz) {
        Constructor<?> constructor = clazz.getConstructors()[0];
        Parameter[] objParams = constructor.getParameters();
        Object[] args = new Object[objParams.length];

        for (int i = 0; i < objParams.length; i++) {
            Class<?> type = objParams[i].getType();
            String name = objParams[i].getName();
            String[] values = parameters.get(name);

            Object param = null;

            if (values == null) {
                throw new ParseException("Missing parameter " + name);
            }

            if (type.isArray()) {
                int arraySize = values.length;
                param = Array.newInstance(type.getComponentType(), arraySize);

                for (int j = 0; j < arraySize; j++) {
                    Array.set(param, j, new ArgParser().parse(values[j], type.getComponentType()));
                }
            } else {
                param = new ArgParser().parse(values[0], type);
            }

            args[i] = param;
        }

        try {
            return (T) constructor.newInstance(args);
        } catch (Exception e) {
            throw new ParseException("Could not instantiate object");
        }
    }

    private static <T> void validate(T object) {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Set<ConstraintViolation<T>> violations = vf.getValidator().validate(object);

        if (!violations.isEmpty()) {
            throw ValidationException.fromConstraintViolation(violations);
        }
    }
}
