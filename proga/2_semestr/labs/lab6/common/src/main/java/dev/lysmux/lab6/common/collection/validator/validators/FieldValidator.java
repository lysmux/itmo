package dev.lysmux.lab6.common.collection.validator.validators;

import java.lang.annotation.Annotation;

/**
 * Interface for validating fields
 *
 * @param <T> type of constraint annotation
 * @since 1.0
 */
public interface FieldValidator<T extends Annotation> {
    /**
     * Validates field
     *
     * @param annotation constraint annotation
     * @param value      value to validate
     * @return {@code true} if value is valid else {@code false}
     */
    boolean validate(T annotation, Object value);

    /**
     * Generates error message
     *
     * @param annotation constraint annotation
     * @return error message
     */
    String getErrorMessage(T annotation);
}
