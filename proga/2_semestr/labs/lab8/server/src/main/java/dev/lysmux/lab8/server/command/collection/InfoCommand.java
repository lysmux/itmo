package dev.lysmux.lab8.server.command.collection;


import dev.lysmux.lab8.server.collection.CollectionManager;
import dev.lysmux.lab8.common.command.Command;
import dev.lysmux.lab8.common.dto.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Command that returns meta information about collection
 *
 * @since 1.0
 */
@Command(name = "info", description = "Display info about collection", requiresLogin = true)
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
