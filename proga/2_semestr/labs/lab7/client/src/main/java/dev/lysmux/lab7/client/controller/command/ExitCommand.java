package dev.lysmux.lab7.client.controller.command;


import dev.lysmux.lab7.common.command.Command;
import dev.lysmux.lab7.common.dto.Response;

/**
 * Command that exits from program
 *
 * @since 1.0
 */
@Command(name = "exit", description = "Exit from program")
final public class ExitCommand {
    /**
     * Exit from program
     *
     * @return execution result
     */
    public Response execute() {
        System.exit(0);
        return Response.builder()
                .text("Exit from program")
                .build();
    }
}
