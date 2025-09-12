package dev.lysmux.lab7.server.command.admin;


import dev.lysmux.lab7.common.command.Command;
import dev.lysmux.lab7.common.dto.Response;
import lombok.RequiredArgsConstructor;

/**
 * Command that exits from program
 *
 * @since 1.0
 */
@Command(name = "exit", description = "Exit from program", local = true)
@RequiredArgsConstructor
final public class ExitCommand {
    /**
     * Exit from program
     * <p>If collection not saved returns error</p>
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
