package dev.lysmux.lab5.controller.command;

import dev.lysmux.lab5.collection.CollectionManager;
import dev.lysmux.lab5.controller.Command;
import dev.lysmux.lab5.controller.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Command that restores collection from backup
 *
 * @since 1.0
 */
@Command(name = "restore", description = "Restore collection", includesInHelp = false)
@RequiredArgsConstructor
final public class RestoreBackupCommand {
    @NonNull
    private final CollectionManager collectionManager;

    /**
     * Restores collection from backup
     *
     * @return execution result
     */
    public Response execute() {
        if (!collectionManager.backupExists()) return Response.of("Backup not found");

        collectionManager.restoreBackup();
        return Response.of("Collection restored");
    }
}
