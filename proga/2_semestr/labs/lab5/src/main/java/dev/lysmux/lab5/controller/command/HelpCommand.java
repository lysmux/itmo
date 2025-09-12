package dev.lysmux.lab5.controller.command;

import dev.lysmux.lab5.controller.Command;
import dev.lysmux.lab5.controller.CommandManager;
import dev.lysmux.lab5.controller.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Command that returns all registered commands and their descriptions
 *
 * @since 1.0
 */
@Command(name = "help", description = "Show help")
@RequiredArgsConstructor
final public class HelpCommand {
    @NonNull
    private final CommandManager commandManager;

    /**
     * Returns all registered commands and their descriptions
     *
     * @return execution result
     */
    public Response execute() {
        StringBuilder sb = new StringBuilder();
        sb.append("Available commands:").append(System.lineSeparator());
        sb.append("=".repeat(40)).append(System.lineSeparator());

        for (var cmd : commandManager.getCommands()) {
            if (!cmd.includesInHelp()) continue;
            sb.append("%s - %s".formatted(cmd.getName(), cmd.getDescription())).append("\n");
        }
        sb.append("=".repeat(40)).append(System.lineSeparator());

        return Response.of(sb.toString());
    }
}
