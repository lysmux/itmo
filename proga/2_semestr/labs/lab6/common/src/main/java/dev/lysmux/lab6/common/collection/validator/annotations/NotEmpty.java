package dev.lysmux.lab6.common.collection.validator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marks field that it should not be {@code empty}
 * <p>Works with {@code String}, {@code Collection} and {@code Map}</p>
 *
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotEmpty {
}
