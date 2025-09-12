package dev.lysmux.lab7.common.command;

import dev.lysmux.lab7.common.command.exception.CommandNotFoundException;
import dev.lysmux.lab7.common.command.wrapper.CommandWrapper;
import dev.lysmux.lab7.common.command.wrapper.LocalCommandWrapper;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


/**
 * Registry for storing registered commands
 *
 * @since 1.0
 */
@Getter
public class CommandRegistry {
    private final Map<String, CommandWrapper> commands = new HashMap<>();

    /**
     * Registers command wrapper in registry
     *
     * @param command command wrapper to register
     * @see CommandWrapper
     */
    public void addCommand(CommandWrapper command) {
        commands.putIfAbsent(command.getName(), command);
    }

    /**
     * Registers command in registry
     *
     * @param command command to register
     * @see Command
     */
    public void addCommand(Object command) {
        addCommand(new LocalCommandWrapper(command));
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
        return commands.get(commandName);
    }
}
