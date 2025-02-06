package dev.lysmux.lab5.controller.command;


import dev.lysmux.lab5.collection.CollectionManager;
import dev.lysmux.lab5.collection.model.LabWork;
import dev.lysmux.lab5.controller.Command;
import dev.lysmux.lab5.controller.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


/**
 * Command that updates collection entity
 *
 * @since 1.0
 */
@Command(name = "update", description = "Update collection with given ID")
@RequiredArgsConstructor
final public class UpdateCommand {
    @NonNull
    private final CollectionManager collectionManager;

    /**
     * Updates collection entity
     *
     * @param entity entity to update
     * @return execution result
     */
    public Response execute(LabWork entity) {
        if (!collectionManager.exists(entity.getId())) {
            return Response.of("Entity not found");
        }
        collectionManager.update(entity);

        return Response.of("Entity updated");
    }
}
