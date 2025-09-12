package dev.lysmux.lab5.controller.command;


import dev.lysmux.lab5.collection.CollectionManager;
import dev.lysmux.lab5.collection.model.LabWork;
import dev.lysmux.lab5.controller.Command;
import dev.lysmux.lab5.controller.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Command that removes all collection items lower than the specified
 *
 * @since 1.0
 */
@Command(
        name = "remove_lower",
        description = "Remove all items lower than the specified"
)
@RequiredArgsConstructor
final public class RemoveLowerCommand {
    @NonNull
    private final CollectionManager collectionManager;

    /**
     * Removes all collection items lower than the specified
     *
     * @param entity collection entity to compare
     * @return execution result
     */
    public Response execute(LabWork entity) {
        Set<Integer> idsToRemove = collectionManager.getCollection().stream()
                .filter(el -> el.compareTo(entity) < 0)
                .map(LabWork::getId)
                .collect(Collectors.toSet());
        if (idsToRemove.isEmpty()) return Response.of("Entities to remove not found");
        idsToRemove.forEach(collectionManager::remove);

        return Response.of("%d entities removed".formatted(idsToRemove.size()));
    }
}
