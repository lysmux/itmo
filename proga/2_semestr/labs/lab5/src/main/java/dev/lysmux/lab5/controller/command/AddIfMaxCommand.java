package dev.lysmux.lab5.controller.command;


import dev.lysmux.lab5.collection.CollectionManager;
import dev.lysmux.lab5.collection.model.LabWork;
import dev.lysmux.lab5.controller.Command;
import dev.lysmux.lab5.controller.Response;
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
        description = "Add a new element to a collection if its value exceeds the value of the largest item in that collection"
)
@RequiredArgsConstructor
final public class AddIfMaxCommand {
    @NonNull
    private final CollectionManager collectionManager;

    /**
     * Adds entity to the collection if its value exceeds
     * the value of the largest item in that collection
     *
     * @param entity entity which should be added to the collection
     * @return execution result
     */
    public Response execute(LabWork entity) {
        LabWork maxEntity = collectionManager.getCollection().stream().max(LabWork::compareTo).orElse(null);
        if (maxEntity == null || maxEntity.compareTo(entity) < 0) {
            collectionManager.add(entity);
            return Response.of("Entity added");
        }

        return Response.of("Entity not added");
    }
}
