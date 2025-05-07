package dev.lysmux.lab8.common.command.wrapper;

import dev.lysmux.lab8.common.command.exception.CouldNotExecuteCommand;
import dev.lysmux.lab8.common.command.meta.CommandArg;
import dev.lysmux.lab8.common.dto.Response;

import java.util.List;


/**
 * Interface that provides information about command and execute method
 *
 * @since 1.0
 */
public interface CommandWrapper {
    /**
     * Executes command with specified arguments
     *
     * @param args array of command arguments
     * @return execution result
     * @throws CouldNotExecuteCommand if command could not be executed with this arguments
     */
    Response execute(Object... args);

    /**
     * Gets command name
     *
     * @return command name
     */
    String getName();

    /**
     * Gets command description
     *
     * @return command description
     */
    String getDescription();

    /**
     * Checks if command needed to include in help
     *
     * @return {@code true} if needed else {@code false}
     */
    boolean includeInHelp();

    /**
     * Checks if command can be executed only local
     *
     * @return {@code true} if can else {@code false}
     */
    boolean local();

    boolean requiresLogin();

    /**
     * Gets command arguments
     *
     * @return list of argument name and its type
     */
    List<CommandArg> getArgs();
}
