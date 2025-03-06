package dev.lysmux.lab6.server.controller.command;

import dev.lysmux.lab6.common.command.Command;
import dev.lysmux.lab6.common.command.CommandRegistry;
import dev.lysmux.lab6.common.command.meta.CommandInfo;
import dev.lysmux.lab6.common.dto.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Command that returns all registered commands and their descriptions
 *
 * @since 1.0
 */
@Command(name = "get_commands", description = "gets commands", includeInHelp = false)
@RequiredArgsConstructor
final public class GetCommandsCommand {
    @NonNull
    private final CommandRegistry commandRegistry;

    /**
     * Returns all registered commands and their descriptions
     *
     * @return execution result
     */
    public Response execute() {
        List<CommandInfo> commands = commandRegistry
                .getCommands().values().stream()
                .filter(cmd -> !cmd.local())
                .map(cmd -> new CommandInfo(
                        cmd.getName(),
                        cmd.getDescription(),
                        cmd.getArgs())
                ).toList();

        return Response.builder()
                .objects(commands)
                .build();
    }
}
