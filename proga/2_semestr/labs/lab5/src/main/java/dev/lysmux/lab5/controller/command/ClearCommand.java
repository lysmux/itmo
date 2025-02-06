package dev.lysmux.lab5.controller.command;


import dev.lysmux.lab5.collection.CollectionManager;
import dev.lysmux.lab5.controller.Command;
import dev.lysmux.lab5.controller.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Command that clears the collection
 *
 * @since 1.0
 */
@Command(name = "clear", description = "Clear collection")
@RequiredArgsConstructor
final public class ClearCommand {
    @NonNull
    private final CollectionManager collectionManager;

    /**
     * Clears the collection
     *
     * @return execution result
     */
    public Response execute() {
        collectionManager.clear();
        return Response.of("Collection cleared");
    }
}
