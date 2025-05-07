package dev.lysmux.lab8.server.command.collection;


import dev.lysmux.lab8.server.collection.CollectionManager;
import dev.lysmux.lab8.common.command.Command;
import dev.lysmux.lab8.common.dto.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Command that returns all collection entities
 *
 * @since 1.0
 */
@Command(name = "show", description = "Display collection elements", requiresLogin = true)
@RequiredArgsConstructor
final public class ShowCommand {
    @NonNull
    private final CollectionManager collectionManager;

    /**
     * Returns all collection entities
     *
     * @return execution result
     */
    public Response execute() {
        if (collectionManager.getCollection().isEmpty()) {
            return Response
                    .builder()
                    .text("Collection is empty")
                    .success(false)
                    .build();
        }

        return Response
                .builder()
                .text("Entities:")
                .objects(List.copyOf(collectionManager.getCollection()))
                .build();
    }
}
