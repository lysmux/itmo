package dev.lysmux.lab6.server.controller.command.admin;


import dev.lysmux.lab6.common.command.Command;
import dev.lysmux.lab6.common.dto.Response;
import dev.lysmux.lab6.server.collection.CollectionManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Command that force exits from program and deletes backup
 *
 * @since 1.0
 */
@Command(name = "force_exit", description = "Force exit from program", local = true)
@RequiredArgsConstructor
final public class ForceExitCommand {
    @NonNull
    private final CollectionManager collectionManager;

    /**
     * Force exits from program and deletes backup
     *
     * @return execution result
     */
    public Response execute() {
        collectionManager.deleteBackup();
        System.exit(0);
        return Response.builder()
                .text("Exit from program")
                .build();
    }
}
