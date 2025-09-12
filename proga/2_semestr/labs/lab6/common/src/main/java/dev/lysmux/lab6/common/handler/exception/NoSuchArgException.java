package dev.lysmux.lab6.common.handler.exception;


import dev.lysmux.lab6.common.command.wrapper.CommandWrapper;

import java.util.stream.Collectors;

/**
 * Exception that throws if required command arg not provided in request
 */
public class NoSuchArgException extends Exception {
    private final CommandWrapper command;

    /**
     * Create exception
     */
    public NoSuchArgException(CommandWrapper command, String argName) {
        super("No such argument `%s`".formatted(argName));
        this.command = command;
    }

    public String getUsage() {
        return "Usage: %s %s".formatted(
                command.getName(),
                command.getArgs().stream()
                        .map(e -> "<%s:%s>".formatted(e.name(), e.type().getSimpleName()))
                        .collect(Collectors.joining(" "))
        );
    }
}
