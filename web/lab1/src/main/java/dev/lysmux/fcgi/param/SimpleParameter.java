package dev.lysmux.fcgi.param;

import dev.lysmux.fcgi.param.parser.ArgParser;

public class SimpleParameter implements Parameter<String>{
    private final String value;

    public SimpleParameter(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }

    public String toString() {
        return value;
    }

    public <T> T asType(Class<T> type) {
        return new ArgParser().parse(this.value, type);
    }
}
