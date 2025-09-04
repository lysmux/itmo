package dev.lysmux.fcgi.annotations;

import dev.lysmux.fcgi.enums.HTTPMethod;
import dev.lysmux.fcgi.enums.HTTPStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RouteMapping {
    String path() default "/";

    HTTPMethod[] methods() default {};

    HTTPStatus status() default HTTPStatus.OK;
}
