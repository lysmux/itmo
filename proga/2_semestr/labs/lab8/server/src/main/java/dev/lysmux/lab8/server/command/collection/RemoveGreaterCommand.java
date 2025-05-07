package dev.lysmux.lab8.server.command.collection;


import dev.lysmux.lab8.server.RequestContext;
import dev.lysmux.lab8.server.collection.CollectionManager;
import dev.lysmux.lab8.common.collection.model.LabWork;
import dev.lysmux.lab8.common.command.Command;
import dev.lysmux.lab8.common.dto.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Command that removes all collection items greater than the specified
 *
 * @since 1.0
 */
@Command(name = "remove_greater", description = "Remove all items from the collection that exceed the specified size", requiresLogin = true)
@RequiredArgsConstructor
final public class RemoveGreaterCommand {
    @NonNull
    private final CollectionManager collectionManager;

    private final RequestContext requestContext;

    /**
     * Removes all collection items greater than the specified
     *
     * @param entity collection entity to compare
     * @return execution result
     */
    public Response execute(LabWork entity) {
        Set<Integer> idsToRemove = collectionManager.getCollection().stream()
                .filter(el -> el.compareTo(entity) > 0 && el.getOwnerId() == requestContext.getUser().id())
                .map(LabWork::getId)
                .collect(Collectors.toSet());
        if (idsToRemove.isEmpty()) return Response.builder()
                .text("Entities to remove not found")
                .success(false)
                .build();
        idsToRemove.forEach(collectionManager::remove);

        return Response.builder()
                .text("%d entities removed".formatted(idsToRemove.size()))
                .build();
    }
}
