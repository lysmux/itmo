package dev.lysmux.lab5.handler.exception;


import dev.lysmux.lab5.controller.CommandWrapper;

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
                command.getArgs().entrySet().stream()
                        .map(e -> "<%s:%s>".formatted(e.getKey(), e.getValue().getSimpleName()))
                        .collect(Collectors.joining(" "))
        );
    }
}
