package dev.lysmux.exception;

import com.google.gson.Gson;
import dev.lysmux.ResponseStatus;
import dev.lysmux.ValidationException;
import dev.lysmux.dto.APIResponse;
import dev.lysmux.dto.ErrorResponse;
import dev.lysmux.parser.exception.ParseException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.java.Log;

import java.io.IOException;

@Log
@WebFilter(urlPatterns = "/*")
public class ExceptionsFilter implements Filter {
    private final Gson gson = new Gson();
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            ErrorResponse<?> errorResponse = handleException(e);
            APIResponse<ErrorResponse<?>> apiResponse = new APIResponse<>(ResponseStatus.ERROR, errorResponse);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(gson.toJson(apiResponse));
        }
    }
    
    private ErrorResponse<?> handleException(Exception e) {
        if (e instanceof ParseException) {
            return ErrorResponse.builder()
                    .type("parseError")
                    .message(e.getMessage())
                    .build();
        } else if (e instanceof ValidationException) {
            return ErrorResponse.builder()
                    .type("validationError")
                    .message(e.getMessage())
                    .details(((ValidationException) e).getViolations())
                    .build();
        }

        return ErrorResponse.builder()
                .type("error")
                .message(e.getMessage())
                .build();
    }
}
