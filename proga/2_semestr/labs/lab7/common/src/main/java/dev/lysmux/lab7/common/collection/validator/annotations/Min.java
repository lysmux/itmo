package dev.lysmux.lab7.common.collection.validator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marks field that it should not be lower that min value
 *
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Min {
    /**
     * Min value of field
     *
     * @return min value
     */
    int value();
}
