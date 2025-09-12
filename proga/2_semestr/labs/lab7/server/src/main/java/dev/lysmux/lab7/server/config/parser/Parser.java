package dev.lysmux.lab7.server.config.parser;

public interface Parser<T> {
    T parse(String input);
}
