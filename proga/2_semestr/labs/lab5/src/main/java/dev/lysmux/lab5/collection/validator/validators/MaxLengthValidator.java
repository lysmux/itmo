package dev.lysmux.lab5.collection.validator.validators;

import dev.lysmux.lab5.collection.validator.annotations.MaxLength;

/**
 * Max length validator. Implementation of {@link FieldValidator} interface
 * <p>Validator checks that length of string not longer than max length</p>
 *
 * @see MaxLength
 * @since 1.0
 */
final public class MaxLengthValidator implements FieldValidator<MaxLength> {
    /**
     * Validates that length of string not longer than max length
     *
     * @param annotation constraint annotation
     * @param value      value to validate
     * @return {@inheritDoc}
     * @see MaxLength
     */
    @Override
    public boolean validate(MaxLength annotation, Object value) {
        if (value == null) return false;
        if (!(value instanceof String)) return false;

        return ((String) value).length() <= annotation.value();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getErrorMessage(MaxLength annotation) {
        return "Value length must be lower than %d".formatted(annotation.value());
    }
}
