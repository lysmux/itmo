package dev.lysmux.lab6.common.collection.validator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marks field that it should not be longer than max length
 * <p>Works only with {@code String}</p>
 *
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MaxLength {
    /**
     * Max length of field
     *
     * @return max length
     */
    long value();
}
