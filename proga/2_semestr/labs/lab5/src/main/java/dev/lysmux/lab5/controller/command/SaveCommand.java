package dev.lysmux.lab5.controller.command;

import dev.lysmux.lab5.collection.CollectionManager;
import dev.lysmux.lab5.controller.Command;
import dev.lysmux.lab5.controller.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Command that saves collection
 *
 * @since 1.0
 */
@Command(name = "save", description = "Save collection")
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
        return Response.of("Collection saved");
    }
}
