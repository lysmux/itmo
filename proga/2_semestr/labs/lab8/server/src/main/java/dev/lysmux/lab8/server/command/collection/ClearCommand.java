package dev.lysmux.lab8.server.command.collection;


import dev.lysmux.lab8.server.RequestContext;
import dev.lysmux.lab8.server.collection.CollectionManager;
import dev.lysmux.lab8.common.command.Command;
import dev.lysmux.lab8.common.dto.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Command that clears the collection
 *
 * @since 1.0
 */
@Command(name = "clear", description = "Clear collection", requiresLogin = true)
@RequiredArgsConstructor
final public class ClearCommand {
    @NonNull
    private final CollectionManager collectionManager;

    private final RequestContext requestContext;

    /**
     * Clears the collection
     *
     * @return execution result
     */
    public Response execute() {
        collectionManager.clear(requestContext.getUser().id());
        return Response
                .builder()
                .text("Collection cleared")
                .build();
    }
}
