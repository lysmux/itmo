package dev.lysmux.lab6.server.controller.command;


import dev.lysmux.lab6.common.collection.model.LabWork;
import dev.lysmux.lab6.common.command.Command;
import dev.lysmux.lab6.common.dto.Response;
import dev.lysmux.lab6.server.collection.CollectionManager;
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
)
@RequiredArgsConstructor
final public class RemoveAnyByMinimalPointCommand {
    @NonNull
    private final CollectionManager collectionManager;

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
        collectionManager.remove(entity.getId());

        return Response.builder()
                .text("Entity removed")
                .build();
    }
}
