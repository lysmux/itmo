package dev.lysmux.lab5.controller.command;

import dev.lysmux.lab5.collection.CollectionManager;
import dev.lysmux.lab5.controller.Command;
import dev.lysmux.lab5.controller.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Command that exits from program
 *
 * @since 1.0
 */
@Command(name = "exit", description = "Exit from program")
@RequiredArgsConstructor
final public class ExitCommand {
    @NonNull
    private final CollectionManager collectionManager;

    /**
     * Exit from program
     * <p>If collection not saved returns error</p>
     *
     * @return execution result
     */
    public Response execute() {
        if (!collectionManager.isCollectionSaved()) {
            return Response.of("!! Collection not saved. Type `force_exit` to exit without saving`");
        }

        System.exit(0);
        return Response.of("Exit from program");
    }
}
