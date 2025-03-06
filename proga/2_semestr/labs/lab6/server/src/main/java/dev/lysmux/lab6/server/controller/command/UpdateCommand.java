package dev.lysmux.lab6.server.controller.command;


import dev.lysmux.lab6.common.collection.model.LabWork;
import dev.lysmux.lab6.common.command.Command;
import dev.lysmux.lab6.common.dto.Response;
import dev.lysmux.lab6.server.collection.CollectionManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


/**
 * Command that updates collection entity
 *
 * @since 1.0
 */
@Command(name = "update", description = "Update collection with given ID")
@RequiredArgsConstructor
final public class UpdateCommand {
    @NonNull
    private final CollectionManager collectionManager;

    /**
     * Updates collection entity
     *
     * @param entity entity to update
     * @return execution result
     */
    public Response execute(LabWork entity) {
        if (!collectionManager.exists(entity.getId())) {
            return Response
                    .builder()
                    .text("Entity not found")
                    .success(false)
                    .build();
        }
        collectionManager.update(entity);

        return Response
                .builder()
                .text("Entity updated")
                .build();
    }
}
