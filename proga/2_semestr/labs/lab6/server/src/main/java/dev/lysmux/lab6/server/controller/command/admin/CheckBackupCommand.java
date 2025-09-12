package dev.lysmux.lab6.server.controller.command.admin;


import dev.lysmux.lab6.common.command.Command;
import dev.lysmux.lab6.common.dto.Response;
import dev.lysmux.lab6.server.collection.CollectionManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Command(name = "check_backup", description = "Checks backup exists", includeInHelp = false, local = true)
@RequiredArgsConstructor
public class CheckBackupCommand {
    @NonNull
    private final CollectionManager collectionManager;

    public Response execute() {
        if (collectionManager.backupExists()) {
            return Response.builder()
                    .text(
                            "Backup exists. You can restore by type `restore` or you can delete it by type `remove_backup`"
                    ).build();
        }
        return Response.builder().build();
    }
}
