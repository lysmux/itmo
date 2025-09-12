package dev.lysmux.lab7.server.command.collection;


import dev.lysmux.lab7.server.RequestContext;
import dev.lysmux.lab7.server.collection.CollectionManager;
import dev.lysmux.lab7.common.collection.model.LabWork;
import dev.lysmux.lab7.common.command.Command;
import dev.lysmux.lab7.common.dto.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Command that adds entity to the collection
 * if its value exceeds the value of the largest item in that collection
 *
 * @since 1.0
 */
@Command(
        name = "add_if_max",
        description = "Add a new element to a collection if its value exceeds the value of the largest item in that collection",
        requiresLogin = true
)
@RequiredArgsConstructor
final public class AddIfMaxCommand {
    @NonNull
    private final CollectionManager collectionManager;

    private final RequestContext requestContext;

    /**
     * Adds entity to the collection if its value exceeds
     * the value of the largest item in that collection
     *
     * @param entity entity which should be added to the collection
     * @return execution result
     */
    public Response execute(LabWork entity) {
        entity.setOwnerId(requestContext.getUser().id());

        LabWork maxEntity = collectionManager.getCollection().stream().max(LabWork::compareTo).orElse(null);
        if (maxEntity == null || maxEntity.compareTo(entity) < 0) {
            collectionManager.add(entity);
            return Response.builder().text("Entity added").build();
        }

        return Response.builder().text("Entity not added").build();
    }
}
