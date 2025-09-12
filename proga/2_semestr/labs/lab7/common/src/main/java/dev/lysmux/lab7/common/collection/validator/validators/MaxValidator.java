package dev.lysmux.lab7.common.collection.validator.validators;

import dev.lysmux.lab7.common.collection.validator.annotations.Max;

/**
 * Max validator. Implementation of {@link FieldValidator} interface
 * <p>Validator checks that value not greater than max value</p>
 *
 * @see Max
 * @since 1.0
 */
final public class MaxValidator implements FieldValidator<Max> {
    /**
     * Validates that value not greater than max value
     *
     * @param annotation constraint annotation
     * @param value      value to validate
     * @return {@inheritDoc}
     * @see Max
     */
    @Override
    public boolean validate(Max annotation, Object value) {
        if (value == null) return false;
        if (!(value instanceof Number)) return false;

        long maxValue = annotation.value();
        return ((Number) value).longValue() < maxValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getErrorMessage(Max annotation) {
        return "Value must be lower than %d".formatted(annotation.value());
    }
}
