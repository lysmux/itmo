package dev.lysmux.lab5.collection.validator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marks field that it should not be shorter than min length
 * <p>Works only with {@code String}</p>
 *
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MinLength {
    /**
     * Min length of field
     *
     * @return min length
     */
    int value();
}
