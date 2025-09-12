package dev.lysmux.lab7.client.controller.command;


import dev.lysmux.lab7.common.command.Command;
import dev.lysmux.lab7.common.command.CommandRegistry;
import dev.lysmux.lab7.common.dto.Response;
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
    private final CommandRegistry commandRegistry;

    /**
     * Returns all registered commands and their descriptions
     *
     * @return execution result
     */
    public Response execute() {
        StringBuilder sb = new StringBuilder();
        sb.append("Available commands:").append(System.lineSeparator());
        sb.append("=".repeat(40)).append(System.lineSeparator());

        for (var cmd : commandRegistry.getCommands().values()) {
            if (!cmd.includeInHelp()) continue;
            sb.append("%s - %s".formatted(cmd.getName(), cmd.getDescription())).append("\n");
        }
        sb.append("=".repeat(40)).append(System.lineSeparator());

        return Response.builder()
                .text(sb.toString())
                .build();
    }
}
