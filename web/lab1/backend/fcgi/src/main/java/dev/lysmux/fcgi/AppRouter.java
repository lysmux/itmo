package dev.lysmux.fcgi;

import dev.lysmux.fcgi.dto.Request;
import dev.lysmux.fcgi.dto.Response;
import dev.lysmux.fcgi.exception.RouteNotFoundException;
import dev.lysmux.fcgi.exception.handlers.DefaultExceptionHandler;
import dev.lysmux.fcgi.exception.handlers.ExceptionHandler;
import dev.lysmux.fcgi.exception.handlers.RouteNotFoundExceptionHandler;
import dev.lysmux.fcgi.exception.handlers.ValidationExceptionHandler;
import dev.lysmux.fcgi.param.parser.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AppRouter extends Router {
    private final Map<Class<? extends Exception>, ExceptionHandler<?>> exceptionHandlers = new HashMap<>();

    @Override
    public void setParent(Router parent) {
        throw new UnsupportedOperationException("AppRouter cannot have a parent");
    }

    public <T extends Exception> void registerExceptionHandler(Class<T> exceptionClass, ExceptionHandler<T> handler) {
        exceptionHandlers.put(exceptionClass, handler);
    }

    @SuppressWarnings("unchecked")
    private <T extends Exception> Optional<ExceptionHandler<T>> findExceptionHandler(Class<T> exceptionClass) {
        ExceptionHandler<?> handler = exceptionHandlers.get(exceptionClass);
        if (handler != null) {
            return Optional.of((ExceptionHandler<T>) handler);
        }

        Class<?> superclass = exceptionClass.getSuperclass();
        while (superclass != null && superclass != Object.class) {
            handler = exceptionHandlers.get(superclass);
            if (handler != null) {
                return Optional.of((ExceptionHandler<T>) handler);
            }

            superclass = superclass.getSuperclass();
        }

        return Optional.empty();
    }

    private <T extends Exception> Response handleException(T e) {
        @SuppressWarnings("unchecked")
        Class<T> exceptionClass = (Class<T>) e.getClass();
        Optional<ExceptionHandler<T>> handler = findExceptionHandler(exceptionClass);

        return handler.orElse(new DefaultExceptionHandler<>()).handle(e);
    }

    public Response handle(Request request) {
        try {
            return route(request);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    {
        registerExceptionHandler(ValidationException.class, new ValidationExceptionHandler());
        registerExceptionHandler(RouteNotFoundException.class, new RouteNotFoundExceptionHandler());
    }
}
