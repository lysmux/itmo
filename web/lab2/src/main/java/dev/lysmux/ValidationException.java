package dev.lysmux;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

public class ValidationException extends RuntimeException {
    @Getter
    private final Set<Violation> violations;

    public record Violation(String field, String message) {
    }

    public ValidationException(Set<Violation> violations) {
        super("Could not validate object");

        this.violations = violations;
    }

    public static <T> ValidationException fromConstraintViolation(Set<ConstraintViolation<T>> violations) {

        return new ValidationException(
                violations.stream()
                        .map(v -> new Violation(v.getPropertyPath().toString(), v.getMessage()))
                        .collect(Collectors.toSet())
        );
    }
}
