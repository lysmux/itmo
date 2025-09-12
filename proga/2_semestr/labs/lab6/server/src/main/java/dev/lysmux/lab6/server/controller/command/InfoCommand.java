package dev.lysmux.lab6.server.controller.command;


import dev.lysmux.lab6.common.command.Command;
import dev.lysmux.lab6.common.dto.Response;
import dev.lysmux.lab6.server.collection.CollectionManager;
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
        return Response.builder()
                .text(collectionManager.getCollectionMeta().toString())
                .build();
    }
}
