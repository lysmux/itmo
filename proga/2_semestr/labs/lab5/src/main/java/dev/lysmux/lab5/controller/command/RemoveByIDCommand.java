package dev.lysmux.lab5.controller.command;

import dev.lysmux.lab5.collection.CollectionManager;
import dev.lysmux.lab5.controller.Command;
import dev.lysmux.lab5.controller.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


/**
 * Command that removes entity from collection by ID
 *
 * @since 1.0
 */
@Command(name = "remove_by_id", description = "Remove collection element by given ID")
@RequiredArgsConstructor
final public class RemoveByIDCommand {
    @NonNull
    private final CollectionManager collectionManager;

    /**
     * Removes entity from collection by ID
     *
     * @param id ID of entity to remove
     * @return execution result
     */
    public Response execute(int id) {
        if (!collectionManager.exists(id)) return Response.of("Entity not found");
        collectionManager.remove(id);

        return Response.of("Entity removed");
    }
}
