package dev.lysmux.exception.handler;

import dev.lysmux.exception.ErrorResponse;
import dev.lysmux.parser.ParseException;
import jakarta.servlet.http.HttpServletResponse;

public class ParseExceptionHandler implements ExceptionHandler<ParseException> {
    @Override
    public ErrorResponse handle(ParseException exception) {
        return new ErrorResponse(
                HttpServletResponse.SC_BAD_REQUEST,
                new ErrorResponse.ErrorDetails(exception.getMessage(), exception.getFieldViolations())
        );
    }
}
