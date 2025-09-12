package dev.lysmux.lab6.server.controller.command.admin;


import dev.lysmux.lab6.common.command.Command;
import dev.lysmux.lab6.common.dto.Response;
import dev.lysmux.lab6.server.collection.CollectionManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Command that restores collection from backup
 *
 * @since 1.0
 */
@Command(name = "restore", description = "Restore collection", includeInHelp = false, local = true)
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
        if (!collectionManager.backupExists()) return Response.builder()
                .text("Backup not found")
                .success(false)
                .build();

        collectionManager.restoreBackup();
        return Response.builder()
                .text("Collection restored")
                .build();
    }
}
