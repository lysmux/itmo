package dev.lysmux.lab5.controller.command;

import dev.lysmux.lab5.collection.CollectionManager;
import dev.lysmux.lab5.controller.Command;
import dev.lysmux.lab5.controller.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Command that removes collection backup
 *
 * @since 1.0
 */
@Command(name = "remove_backup", description = "Remove collection backup", includesInHelp = false)
@RequiredArgsConstructor
final public class RemoveBackupCommand {
    @NonNull
    private final CollectionManager collectionManager;

    /**
     * Removes collection backup
     *
     * @return execution result
     */
    public Response execute() {
        if (!collectionManager.backupExists()) return Response.of("Backup not found");

        collectionManager.restoreBackup();
        return Response.of("Backup removed");
    }
}
