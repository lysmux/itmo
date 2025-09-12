package dev.lysmux.lab8.server.command.collection;


import dev.lysmux.lab8.server.RequestContext;
import dev.lysmux.lab8.server.collection.CollectionManager;
import dev.lysmux.lab8.common.collection.model.LabWork;
import dev.lysmux.lab8.common.command.Command;
import dev.lysmux.lab8.common.dto.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Command that removes one item from the collection, the value of the minimal point field
 * of which is equivalent to the specified one
 *
 * @since 1.0
 */
@Command(
        name = "remove_any_by_minimal_point",
        description = "Remove one item from the collection, the value of the minimal Point field of which is equivalent to the specified one"
        ,requiresLogin = true
)
@RequiredArgsConstructor
final public class RemoveAnyByMinimalPointCommand {
    @NonNull
    private final CollectionManager collectionManager;

    private final RequestContext requestContext;

    /**
     * Removes one item from the collection, the value of the minimal point field
     * of which is equivalent to the specified one
     *
     * @param minimalPoint value to filtering
     * @return execution result
     */
    public Response execute(long minimalPoint) {
        LabWork entity = collectionManager.getCollection().stream()
                .filter(el -> el.getMinimalPoint() == minimalPoint)
                .findFirst().orElse(null);
        if (entity == null) {
            return Response.builder()
                    .text("Entity with this minimal point not found")
                    .success(false)
                    .build();
        }
        if (!collectionManager.hasAccess(entity.getId(), requestContext.getUser().id())) {
            return Response.builder()
                    .text("You do not have access to this collection item")
                    .success(false)
                    .build();
        }
        collectionManager.remove(entity.getId());

        return Response.builder()
                .text("Entity removed")
                .build();
    }
}
