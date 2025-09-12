package dev.lysmux.lab6.server.controller.command;


import dev.lysmux.lab6.common.collection.model.LabWork;
import dev.lysmux.lab6.common.command.Command;
import dev.lysmux.lab6.common.dto.Response;
import dev.lysmux.lab6.server.collection.CollectionManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * Command that returns  values of the minimal point field
 * of all elements in descending order
 *
 * @since 1.0
 */
@Command(
        name = "print_field_descending_minimal_point",
        description = "Display the values of the minimal point field of all elements in descending order"
)
@RequiredArgsConstructor
final public class PrintFieldDescendingMinimalPoint {
    @NonNull
    private final CollectionManager collectionManager;

    /**
     * returns  values of the minimal point field of all elements in descending order
     *
     * @return execution result
     */
    public Response execute() {
        List<Long> minimalPoints = collectionManager.getCollection().stream()
                .map(LabWork::getMinimalPoint)
                .sorted(Collections.reverseOrder())
                .toList();

        return Response.builder()
                .text("Descending points: %s".formatted(minimalPoints.toString()))
                .build();
    }
}
