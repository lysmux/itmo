package dev.lysmux.fcgi.param;

public interface MapParameter<T> extends Parameter<T>{
    Parameter<?> get(String key);
}
