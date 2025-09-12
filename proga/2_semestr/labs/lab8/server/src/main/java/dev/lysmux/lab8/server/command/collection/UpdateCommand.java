package dev.lysmux.lab8.server.command.collection;


import dev.lysmux.lab8.server.RequestContext;
import dev.lysmux.lab8.server.collection.CollectionManager;
import dev.lysmux.lab8.common.collection.model.LabWork;
import dev.lysmux.lab8.common.command.Command;
import dev.lysmux.lab8.common.dto.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


/**
 * Command that updates collection entity
 *
 * @since 1.0
 */
@Command(name = "update", description = "Update collection with given ID", requiresLogin = true)
@RequiredArgsConstructor
final public class UpdateCommand {
    @NonNull
    private final CollectionManager collectionManager;

    private final RequestContext requestContext;

    /**
     * Updates collection entity
     *
     * @param entity entity to update
     * @return execution result
     */
    public Response execute(LabWork entity) {
        if (!collectionManager.exists(entity.getId())) {
            return Response
                    .builder()
                    .text("Entity not found")
                    .success(false)
                    .build();
        }
        if (!collectionManager.hasAccess(entity.getId(), requestContext.getUser().id())) {
            return Response.builder()
                    .text("You do not have access to this collection item")
                    .success(false)
                    .build();
        }
        collectionManager.update(entity);

        return Response
                .builder()
                .text("Entity updated")
                .build();
    }
}
