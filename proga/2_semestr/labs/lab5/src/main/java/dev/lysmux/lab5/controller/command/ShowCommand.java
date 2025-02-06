package dev.lysmux.lab5.controller.command;


import dev.lysmux.lab5.collection.CollectionManager;
import dev.lysmux.lab5.controller.Command;
import dev.lysmux.lab5.controller.Response;
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
            return Response.of("Collection is empty");
        }

        return Response.of("Entities:", collectionManager.getCollection());
    }
}
