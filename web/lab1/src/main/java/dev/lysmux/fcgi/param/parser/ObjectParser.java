package dev.lysmux.fcgi.param.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dev.lysmux.fcgi.param.*;
import dev.lysmux.fcgi.param.parser.exception.FieldRequiredException;
import dev.lysmux.fcgi.param.parser.exception.InvalidValueException;
import dev.lysmux.fcgi.param.parser.exception.ValidationException;

import java.lang.reflect.RecordComponent;
import java.util.StringTokenizer;


public class ObjectParser {
    public static <T> T parseQuery(
            String paramName,
            Class<T> paramType,
            String queryParams
    ) {
        ObjectParameter objectParameter = new ObjectParameter();

        StringTokenizer tokenizer = new StringTokenizer(queryParams, "&");
        while (tokenizer.hasMoreTokens()) {
            String[] pair = tokenizer.nextToken().split("=");
            objectParameter.put(pair[0], new SimpleParameter(pair[1]));
        }

        return parseObject(paramName, paramType, objectParameter);
    }

    public static <T> T parseJson(
            String paramName,
            Class<T> paramType,
            String body
    ) {
        JsonElement json;

        try {
            json = JsonParser.parseString(body);
        } catch (Exception e) {
            throw new ValidationException("Could not parse JSON", e);
        }
        Parameter<?> parameter = JsonParameter.elementToParameter(json);

        return parseObject(paramName, paramType, parameter);
    }

    public static <T> T parseForm(
            String paramName,
            Class<T> paramType,
            String body
    ) {

        return null;
    }

    public static <T> T parsePath(
            String paramName,
            Class<T> paramType,
            String routePath,
            String requestPath
    ) {
        ObjectParameter objectParameter = new ObjectParameter();

        StringTokenizer routePathTokenizer = new StringTokenizer(routePath, "/");
        StringTokenizer requestPathTokenizer = new StringTokenizer(requestPath, "/");

        while (routePathTokenizer.hasMoreTokens()) {
            String routeToken = routePathTokenizer.nextToken();
            String requestToken = requestPathTokenizer.nextToken();
            if (routeToken.startsWith(":")) {
                objectParameter.put(routeToken.substring(1), new SimpleParameter(requestToken));
            }
        }

        return parseObject(paramName, paramType, objectParameter);
    }

    public static <T> T parseObject(
            String paramName,
            Class<T> paramType,
            Parameter<?> parameter
    ) {
        T param = null;

        if (parameter == null) {
            throw new FieldRequiredException(paramName);
        }

        if (paramType.isRecord()) {
            if (parameter instanceof MapParameter) {
                param = parseRecord(paramType, (MapParameter<?>) parameter);
            }
        } else if (paramType.isArray()) {
            if (parameter instanceof ArrayParameter) {
                ArrayParameter<?> arrayParameter = (ArrayParameter<?>) parameter;

                param = (T) arrayParameter.get().stream()
                        .map(p -> parseObject(paramName, paramType.getComponentType(), p))
                        .toArray();

            }
        } else {
            if (parameter instanceof SimpleParameter) {
                param = ((SimpleParameter) parameter).asType(paramType);
            } else if (parameter instanceof MapParameter<?>) {
                Parameter<?> p = ((MapParameter<?>) parameter).get(paramName);
                if (p instanceof SimpleParameter) {
                    param = ((SimpleParameter) p).asType(paramType);
                } else if (p instanceof ArrayParameter<?>) {
                    Parameter<?> p2 = ((ArrayParameter<?>) p).get().getLast();
                    if (p2 instanceof SimpleParameter) {
                        param = ((SimpleParameter) p2).asType(paramType);
                    }
                }
            }
        }

        if (param == null) {
            throw new InvalidValueException(paramName, parameter.getClass(), paramType);
        }

        return param;
    }

    private static <T> T parseRecord(
            Class<T> paramType,
            MapParameter<?> parameter
    ) {
        Object[] args = new Object[paramType.getRecordComponents().length];

        for (int i = 0; i < args.length; i++) {
            RecordComponent recordComponent = paramType.getRecordComponents()[i];
            Class<?> recordParamType = recordComponent.getType();

            Parameter<?> recordParameter = parameter.get(recordComponent.getName());
            if (recordParameter instanceof SimpleParameter) {
                args[i] = ((SimpleParameter) recordParameter).asType(recordParamType);
            } else {
                args[i] = parseObject(recordComponent.getName(), recordParamType, recordParameter);
            }
        }

        T obj;
        try {
            obj = (T) paramType.getDeclaredConstructors()[0].newInstance(args);
        } catch (ReflectiveOperationException e) {
            throw new ValidationException("Could not parse object", e);
        }

        return obj;
    }
}
