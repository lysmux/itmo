package dev.lysmux.fcgi.param;

public interface Parameter<T> {
    T get();

    default boolean isArray() {
        return this instanceof ArrayParameter;
    }
}
