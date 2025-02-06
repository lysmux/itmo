package dev.lysmux.lab5.collection.validator;

/**
 * Constraint violation class
 * <p>Stores information about constraint violation</p>
 *
 * @param message      reason of violation
 * @param invalidValue invalid value
 * @param fieldPath    name of invalid field
 */
public record ConstraintViolation(
        String message,
        Object invalidValue,
        String fieldPath
) {
}
