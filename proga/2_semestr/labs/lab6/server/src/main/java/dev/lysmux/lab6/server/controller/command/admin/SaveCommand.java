package dev.lysmux.lab6.server.controller.command.admin;


import dev.lysmux.lab6.common.command.Command;
import dev.lysmux.lab6.common.dto.Response;
import dev.lysmux.lab6.server.collection.CollectionManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Command that saves collection
 *
 * @since 1.0
 */
@Command(name = "save", description = "Save collection", local = true)
@RequiredArgsConstructor
final public class SaveCommand {
    @NonNull
    private final CollectionManager collectionManager;

    /**
     * Saves collection
     *
     * @return execution result
     */
    public Response execute() {
        collectionManager.save();
        return Response.builder()
                .text("Collection saved")
                .build();
    }
}
