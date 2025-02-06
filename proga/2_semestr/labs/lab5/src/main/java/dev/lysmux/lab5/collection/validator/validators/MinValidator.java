package dev.lysmux.lab5.collection.validator.validators;

import dev.lysmux.lab5.collection.validator.annotations.Min;

/**
 * Min validator. Implementation of {@link FieldValidator} interface
 * <p>Validator checks that value not lower than min value</p>
 *
 * @see Min
 * @since 1.0
 */
final public class MinValidator implements FieldValidator<Min> {
    /**
     * Validates that value not lower than min value
     *
     * @param annotation constraint annotation
     * @param value      value to validate
     * @return {@inheritDoc}
     * @see Min
     */
    @Override
    public boolean validate(Min annotation, Object value) {
        if (value == null) return false;
        if (!(value instanceof Number)) return false;

        long minValue = annotation.value();
        return ((Number) value).longValue() >= minValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getErrorMessage(Min annotation) {
        return "Value must be greater than %d".formatted(annotation.value());
    }
}
