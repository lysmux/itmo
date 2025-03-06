package dev.lysmux.lab6.server.controller.command;

import dev.lysmux.lab6.common.command.Command;
import dev.lysmux.lab6.common.dto.Response;
import dev.lysmux.lab6.server.collection.CollectionManager;
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
        if (!collectionManager.exists(id)) return Response.builder()
                .text("Entity not found")
                .success(false)
                .build();
        collectionManager.remove(id);

        return Response.builder()
                .text("Entity removed")
                .build();
    }
}
