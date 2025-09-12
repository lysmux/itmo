package dev.lysmux.lab6.server.controller.command;


import dev.lysmux.lab6.common.collection.model.LabWork;
import dev.lysmux.lab6.common.command.Command;
import dev.lysmux.lab6.common.dto.Response;
import dev.lysmux.lab6.server.collection.CollectionManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Command that returns the number of elements whose minimum point field value
 * is greater than the specified one
 *
 * @since 1.0
 */
@Command(
        name = "count_greater_than_minimal_point",
        description = "Display the number of elements whose minimum point field value is greater than the specified one"
)
@RequiredArgsConstructor
final public class CountGreaterThanMinimalPointCommand {
    @NonNull
    private final CollectionManager collectionManager;

    /**
     * Returns the number of elements whose minimum point field value
     * is greater than the specified one
     *
     * @param minimalPoint value for filtering
     * @return execution result
     */
    public Response execute(long minimalPoint) {
        long count = collectionManager.getCollection().stream()
                .map(LabWork::getMinimalPoint)
                .filter(el -> el > minimalPoint)
                .count();

        return Response
                .builder()
                .text("Minimal point: %d".formatted(count))
                .build();
    }
}
