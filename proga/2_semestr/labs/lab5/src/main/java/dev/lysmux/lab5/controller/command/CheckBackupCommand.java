package dev.lysmux.lab5.controller.command;

import dev.lysmux.lab5.collection.CollectionManager;
import dev.lysmux.lab5.controller.Command;
import dev.lysmux.lab5.controller.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Command(name = "check_backup", description = "Checks backup exists", includesInHelp = false)
@RequiredArgsConstructor
public class CheckBackupCommand {
    @NonNull
    private final CollectionManager collectionManager;

    public Response execute() {
        if (collectionManager.backupExists()) {
            return Response.of(
                    "Backup exists. You can restore by type `restore` or you can delete it by type `remove_backup`"
            );
        }
        return Response.empty();
    }
}
