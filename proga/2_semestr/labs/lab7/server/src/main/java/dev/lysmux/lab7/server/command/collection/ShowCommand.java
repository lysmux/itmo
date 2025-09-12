package dev.lysmux.lab7.server.command.collection;


import dev.lysmux.lab7.server.collection.CollectionManager;
import dev.lysmux.lab7.common.command.Command;
import dev.lysmux.lab7.common.dto.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
                .objects(collectionManager.getCollection())
                .build();
    }
}
