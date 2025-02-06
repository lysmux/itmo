package dev.lysmux.lab5.controller;

import dev.lysmux.lab5.controller.exception.CouldNotExecuteCommand;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * Class that provides access to information about the command
 * through Java reflection API
 *
 * @since 1.0
 */
@RequiredArgsConstructor
public class CommandWrapper {
    @NonNull
    private final Object command;

    /**
     * Executes command with specified arguments
     *
     * @param args array of command arguments
     * @return execution result
     * @throws CouldNotExecuteCommand if command could not be executed with this arguments
     */
    public Response execute(Object... args) {
        Method executeMethod = getExecuteMethod();
        try {
            return (Response) executeMethod.invoke(command, args);
        } catch (ReflectiveOperationException e) {
            throw new CouldNotExecuteCommand(e);
        }
    }

    /**
     * Gets command name from annotation {@link Command}
     *
     * @return command name
     * @see Command
     */
    public String getName() {
        return command.getClass().getAnnotation(Command.class).name();
    }

    /**
     * Gets command description from annotation {@link Command}
     *
     * @return command description
     * @see Command
     */
    public String getDescription() {
        return command.getClass().getAnnotation(Command.class).description();
    }

    /**
     * Checks if command needed to include in help from annotation {@link Command}
     *
     * @return {@code true} if needed else {@code false}
     * @see Command
     */
    public boolean includesInHelp() {
        return command.getClass().getAnnotation(Command.class).includesInHelp();
    }

    /**
     * Gets command arguments
     *
     * @return map of argument name and its type
     */
    public LinkedHashMap<String, Class<?>> getArgs() {
        LinkedHashMap<String, Class<?>> commandArgs = new LinkedHashMap<>();
        Method executeMethod = getExecuteMethod();

        for (var param : executeMethod.getParameters()) {
            commandArgs.put(param.getName(), param.getType());
        }
        return commandArgs;
    }

    /**
     * Gets command {@code execute} method via reflection
     *
     * @return execute method
     */
    private Method getExecuteMethod() {
        Class<?> clazz = command.getClass();
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.getName().equals("execute"))
                .findFirst()
                .orElse(null);
    }
}
