package dev.lysmux.lab5.controller.command;

import dev.lysmux.lab5.collection.CollectionManager;
import dev.lysmux.lab5.controller.Command;
import dev.lysmux.lab5.controller.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Command that force exits from program and deletes backup
 *
 * @since 1.0
 */
@Command(name = "force_exit", description = "Force exit from program")
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
        return Response.of("Exit from program");
    }
}
