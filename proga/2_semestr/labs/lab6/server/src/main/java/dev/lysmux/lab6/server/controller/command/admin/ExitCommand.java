package dev.lysmux.lab6.server.controller.command.admin;


import dev.lysmux.lab6.common.command.Command;
import dev.lysmux.lab6.common.dto.Response;
import dev.lysmux.lab6.server.collection.CollectionManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Command that exits from program
 *
 * @since 1.0
 */
@Command(name = "exit", description = "Exit from program", local = true)
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
            return Response.builder()
                    .text("!! Collection not saved. Type `force_exit` to exit without saving`")
                    .success(false)
                    .build();
        }

        System.exit(0);
        return Response.builder()
                .text("Exit from program")
                .build();
    }
}
