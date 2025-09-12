package dev.lysmux.lab5.collection.validator.validators;

import dev.lysmux.lab5.collection.validator.annotations.MinLength;

/**
 * Min length validator. Implementation of {@link FieldValidator} interface
 * <p>Validator checks that length of string not shorter than min length</p>
 *
 * @see MinLength
 * @since 1.0
 */
final public class MinLengthValidator implements FieldValidator<MinLength> {
    /**
     * Validates that length of string not shorter than min length
     *
     * @param annotation constraint annotation
     * @param value      value to validate
     * @return {@inheritDoc}
     * @see MinLength
     */
    @Override
    public boolean validate(MinLength annotation, Object value) {
        if (value == null) return false;
        if (!(value instanceof String)) return false;

        long minValue = annotation.value();
        return ((String) value).length() >= minValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getErrorMessage(MinLength annotation) {
        return "Value length must be greater than %d".formatted(annotation.value());
    }
}
