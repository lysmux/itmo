package dev.lysmux.lab8.server.config.parser;

public interface Parser<T> {
    T parse(String input);
}
