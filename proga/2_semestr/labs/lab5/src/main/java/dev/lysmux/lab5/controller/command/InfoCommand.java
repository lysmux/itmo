package dev.lysmux.lab5.controller.command;


import dev.lysmux.lab5.collection.CollectionManager;
import dev.lysmux.lab5.controller.Command;
import dev.lysmux.lab5.controller.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Command that returns meta information about collection
 *
 * @since 1.0
 */
@Command(name = "info", description = "Display info about collection")
@RequiredArgsConstructor
final public class InfoCommand {
    @NonNull
    private final CollectionManager collectionManager;

    /**
     * Returns meta information about collection
     *
     * @return execution result
     */
    public Response execute() {
        return Response.of(collectionManager.getCollectionMeta().toString());
    }
}
