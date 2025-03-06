package dev.lysmux.lab6.server.controller.command;


import dev.lysmux.lab6.common.command.Command;
import dev.lysmux.lab6.common.dto.Response;
import dev.lysmux.lab6.server.collection.CollectionManager;
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
        return Response
                .builder()
                .text("Collection cleared")
                .build();
    }
}
