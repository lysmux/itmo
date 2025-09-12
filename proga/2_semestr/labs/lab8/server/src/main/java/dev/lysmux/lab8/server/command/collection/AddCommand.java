package dev.lysmux.lab8.server.command.collection;

import dev.lysmux.lab8.server.RequestContext;
import dev.lysmux.lab8.server.collection.CollectionManager;
import dev.lysmux.lab8.common.collection.model.LabWork;
import dev.lysmux.lab8.common.command.Command;
import dev.lysmux.lab8.common.dto.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Command that adds entity to the collection
 *
 * @since 1.0
 */
@Command(name = "add", description = "Add entity to collection", requiresLogin = true)
@RequiredArgsConstructor
final public class AddCommand {
    @NonNull
    private final CollectionManager collectionManager;

    private final RequestContext requestContext;

    /**
     * Adds entity to the collection
     *
     * @param entity entity which should be added to the collection
     * @return execution result
     */
    public Response execute(LabWork entity) {
        entity.setOwnerId(requestContext.getUser().id());
        collectionManager.add(entity);
        return Response
                .builder()
                .text("Entity added")
                .build();
    }
}
