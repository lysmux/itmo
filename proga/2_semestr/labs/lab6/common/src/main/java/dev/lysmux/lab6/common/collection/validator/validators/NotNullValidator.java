package dev.lysmux.lab6.common.collection.validator.validators;

import dev.lysmux.lab6.common.collection.validator.annotations.NotNull;

/**
 * Not null validator. Implementation of {@link FieldValidator} interface
 * <p>Validator checks that value not null</p>
 *
 * @see NotNull
 * @since 1.0
 */
final public class NotNullValidator implements FieldValidator<NotNull> {
    /**
     * Validates that value not empty
     *
     * @param annotation constraint annotation
     * @param value      value to validate
     * @return {@inheritDoc}
     * @see NotNull
     */
    @Override
    public boolean validate(NotNull annotation, Object value) {
        return value != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getErrorMessage(NotNull annotation) {
        return "Value must not be null";
    }
}
