package dev.lysmux.lab5.controller.command;

import dev.lysmux.lab5.collection.CollectionManager;
import dev.lysmux.lab5.collection.model.LabWork;
import dev.lysmux.lab5.controller.Command;
import dev.lysmux.lab5.controller.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Command that adds entity to the collection
 *
 * @since 1.0
 */
@Command(name = "add", description = "Add entity to collection")
@RequiredArgsConstructor
final public class AddCommand {
    @NonNull
    private final CollectionManager collectionManager;

    /**
     * Adds entity to the collection
     *
     * @param entity entity which should be added to the collection
     * @return execution result
     */
    public Response execute(LabWork entity) {
        collectionManager.add(entity);
        return Response.of("Entity added");
    }
}
