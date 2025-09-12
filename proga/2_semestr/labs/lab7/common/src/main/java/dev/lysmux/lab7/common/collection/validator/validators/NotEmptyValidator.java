package dev.lysmux.lab7.common.collection.validator.validators;

import dev.lysmux.lab7.common.collection.validator.annotations.NotEmpty;

import java.util.Collection;
import java.util.Map;


/**
 * Not empty validator. Implementation of {@link FieldValidator} interface
 * <p>Validator checks that value not empty</p>
 * <p>Works with {@code String}, {@code Collection} and {@code Map}</p>
 *
 * @see NotEmpty
 * @since 1.0
 */
final public class NotEmptyValidator implements FieldValidator<NotEmpty> {
    /**
     * Validates that value not {@code empty}
     *
     * @param annotation constraint annotation
     * @param value      value to validate
     * @return {@inheritDoc}
     * @see NotEmpty
     */
    @Override
    public boolean validate(NotEmpty annotation, Object value) {
        if (value == null) return false;
        if (value instanceof String && ((String) value).trim().isEmpty()) return false;
        if (value instanceof Collection<?> && ((Collection<?>) value).isEmpty()) return false;
        if (value instanceof Map<?, ?> && ((Map<?, ?>) value).isEmpty()) return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getErrorMessage(NotEmpty annotation) {
        return "Value must be not empty";
    }
}
