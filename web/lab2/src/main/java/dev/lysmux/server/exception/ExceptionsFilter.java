package dev.lysmux.server.exception;

import com.google.gson.Gson;
import dev.lysmux.server.exception.handler.DefaultExceptionHandler;
import dev.lysmux.server.exception.handler.ExceptionHandler;
import dev.lysmux.server.exception.handler.ParseExceptionHandler;
import dev.lysmux.server.parser.ParseException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class ExceptionsFilter implements Filter {
    private final Map<Class<? extends Exception>, ExceptionHandler<?>> exceptionHandlers = new HashMap<>();
    private final Gson gson = new Gson();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            ErrorResponse errorResponse = handleException(e);

            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setStatus(errorResponse.statusCode());
            httpResponse.getWriter().println(gson.toJson(errorResponse.details()));
        }
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

    private <T extends Exception> ErrorResponse handleException(T e) {
        @SuppressWarnings("unchecked")
        Class<T> exceptionClass = (Class<T>) e.getClass();
        Optional<ExceptionHandler<T>> handler = findExceptionHandler(exceptionClass);

        return handler.orElse(new DefaultExceptionHandler<>()).handle(e);
    }

    {
        registerExceptionHandler(ParseException.class, new ParseExceptionHandler());
    }

//    private ErrorResponse<?> handleException(Exception e) {
//        log.error("An exception occurred", e);
//
//        if (e instanceof ParseException) {
//            return ErrorResponse.builder()
//                    .type("validationError")
//                    .message(e.getMessage())
//                    .details(((ParseException) e).getFieldViolations())
//                    .build();
//        }
//
//        return ErrorResponse.builder()
//                .type("error")
//                .message(e.getMessage())
//                .build();
//    }
}
