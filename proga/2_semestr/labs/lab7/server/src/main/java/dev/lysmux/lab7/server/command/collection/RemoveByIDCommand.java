package dev.lysmux.lab7.server.command.collection;

import dev.lysmux.lab7.server.RequestContext;
import dev.lysmux.lab7.server.collection.CollectionManager;
import dev.lysmux.lab7.common.command.Command;
import dev.lysmux.lab7.common.dto.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


/**
 * Command that removes entity from collection by ID
 *
 * @since 1.0
 */
@Command(name = "remove_by_id", description = "Remove collection element by given ID", requiresLogin = true)
@RequiredArgsConstructor
final public class RemoveByIDCommand {
    @NonNull
    private final CollectionManager collectionManager;

    private final RequestContext requestContext;

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
        if (!collectionManager.hasAccess(id, requestContext.getUser().id())) {
            return Response.builder()
                    .text("You do not have access to this collection item")
                    .success(false)
                    .build();
        }
        collectionManager.remove(id);

        return Response.builder()
                .text("Entity removed")
                .build();
    }
}
