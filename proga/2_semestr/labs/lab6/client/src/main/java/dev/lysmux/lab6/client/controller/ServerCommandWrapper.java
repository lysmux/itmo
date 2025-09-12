package dev.lysmux.lab6.client.controller;

import dev.lysmux.lab6.client.network.Client;
import dev.lysmux.lab6.common.command.exception.CouldNotExecuteCommand;
import dev.lysmux.lab6.common.command.meta.CommandArg;
import dev.lysmux.lab6.common.command.meta.CommandInfo;
import dev.lysmux.lab6.common.command.wrapper.CommandWrapper;
import dev.lysmux.lab6.common.dto.Request;
import dev.lysmux.lab6.common.dto.Response;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Class that provides access to the command through client
 *
 * @since 1.0
 */
@RequiredArgsConstructor
public class ServerCommandWrapper implements CommandWrapper {
    private final Client client;
    private final CommandInfo commandInfo;

    /**
     * Executes command with specified arguments
     *
     * @param args array of command arguments
     * @return execution result
     * @throws CouldNotExecuteCommand if command could not be executed with this arguments
     */
    @Override
    public Response execute(Object... args) {
        Request request = new Request(commandInfo.name(), args);
        return client.request(request);
    }

    /**
     * Gets command name from from {@link CommandInfo}
     *
     * @return command name
     * @see CommandInfo
     */
    @Override
    public String getName() {
        return commandInfo.name();
    }

    /**
     * Gets command description from {@link CommandInfo}
     *
     * @return command description
     * @see CommandInfo
     */
    @Override
    public String getDescription() {
        return commandInfo.description();
    }

    /**
     * Checks if command needed to include in help
     * <p>Always true</p>
     *
     * @return {@code true} if needed else {@code false}
     */
    @Override
    public boolean includeInHelp() {
        return true;
    }

    /**
     * Checks if command can be executed only local
     * <p>Always false</p>
     *
     * @return {@code true} if can else {@code false}
     */
    @Override
    public boolean local() {
        return false;
    }

    /**
     * Gets command arguments from {@link CommandInfo}
     *
     * @return list of argument name and its type
     * @see CommandInfo
     */
    @Override
    public List<CommandArg> getArgs() {
        return commandInfo.args();
    }
}
