package dev.lysmux.fcgi.annotations;

import dev.lysmux.fcgi.enums.HTTPParamType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Param {
    String alias() default "";

    HTTPParamType type() default HTTPParamType.JSON;
}
