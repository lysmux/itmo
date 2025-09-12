package dev.lysmux.lab6.common.collection.validator;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Exception that throws if field or model has violation constrains
 */
@Getter
@RequiredArgsConstructor
public class ValidationException extends Exception {
    @NonNull
    private final Set<ConstraintViolation> violations;

    @Override
    public String getMessage() {
        return violations.stream()
                .map(ConstraintViolation::message)
                .collect(Collectors.joining("\n"));
    }
}
