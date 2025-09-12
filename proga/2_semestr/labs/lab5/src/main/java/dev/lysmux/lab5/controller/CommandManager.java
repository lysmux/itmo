package dev.lysmux.lab5.controller;

import dev.lysmux.lab5.controller.exception.CommandNotFoundException;
import dev.lysmux.lab5.controller.exception.InvalidCommandException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Registry for storing registered commands
 *
 * @since 1.0
 */
public class CommandManager {
    private final Map<String, Object> commands = new HashMap<>();

    /**
     * Registers command in registry
     * <p>
     * Command could not be registered if it is not annotated {@link Command} annotation
     * or has more than 1 <b>execute</b> method
     * </p>
     *
     * @param command command to register
     * @throws InvalidCommandException if command could not be registered
     * @see Command
     */
    public void registerCommand(Object command) {
        Class<?> clazz = command.getClass();
        Method[] executeMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.getName().equals("execute")).toArray(Method[]::new);

        if (!clazz.isAnnotationPresent(Command.class)) {
            throw new InvalidCommandException("Command must be annotated with @Command");
        }

        if (executeMethods.length != 1) {
            throw new InvalidCommandException("Command must have exactly one `execute` method");
        }

        if (executeMethods[0].getReturnType() != Response.class) {
            throw new InvalidCommandException("Command must return a `Response` object");
        }

        String commandName = clazz.getAnnotation(Command.class).name();
        commands.put(commandName, command);
    }


    /**
     * Gets command by its name
     *
     * @param commandName name of command to search
     * @return {@link CommandWrapper} for desired command
     * @throws CommandNotFoundException if command not registered
     * @see CommandWrapper
     */
    public CommandWrapper getCommand(String commandName) throws CommandNotFoundException {
        if (!commands.containsKey(commandName)) throw new CommandNotFoundException(commandName);
        return new CommandWrapper(commands.get(commandName));
    }

    /**
     * Gets registered commands
     *
     * @return collection of registered commands
     */
    public List<CommandWrapper> getCommands() {
        return commands.values().stream().map(CommandWrapper::new).collect(Collectors.toList());
    }
}
