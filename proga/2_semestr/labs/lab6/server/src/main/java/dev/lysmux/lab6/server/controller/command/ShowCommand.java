package dev.lysmux.lab6.server.controller.command;


import dev.lysmux.lab6.common.command.Command;
import dev.lysmux.lab6.common.dto.Response;
import dev.lysmux.lab6.server.collection.CollectionManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Command that returns all collection entities
 *
 * @since 1.0
 */
@Command(name = "show", description = "Display collection elements")
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
