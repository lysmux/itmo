package dev.lysmux.lab7.server.command.collection;

import dev.lysmux.lab7.server.RequestContext;
import dev.lysmux.lab7.server.collection.CollectionManager;
import dev.lysmux.lab7.common.collection.model.*;
import dev.lysmux.lab7.common.command.Command;
import dev.lysmux.lab7.common.dto.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Random;

/**
 * Command that adds entity to the collection
 *
 * @since 1.0
 */
@Command(name = "add_random", description = "Add random entity to collection", requiresLogin = true)
@RequiredArgsConstructor
final public class AddRandomCommand {
    @NonNull
    private final CollectionManager collectionManager;

    private final RequestContext requestContext;

    private final Random random = new Random();

    /**
     * Adds random entity to the collection
     *
     * @return execution result
     */
    public Response execute(int count) {
        for (int i = 0; i < count; i++) {
            addRandom();
        }

        return Response.builder()
                .text("%d entities added".formatted(count))
                .build();
    }

    private void addRandom() {
        LabWork entity = new LabWork();
        entity.setOwnerId(requestContext.getUser().id());
        entity.setName("entity_%d".formatted(random.nextInt(1000)));
        entity.setCoordinates(getRandomCoordinates());
        entity.setMinimalPoint(random.nextLong(100) + 1);
        entity.setDifficulty(getRandomDifficulty());
        entity.setAuthor(getRandomPerson());

        collectionManager.add(entity);
    }

    private Coordinates getRandomCoordinates() {
        return new Coordinates(
                random.nextInt(-10, 10),
                random.nextDouble(-10, 10)
        );
    }

    private Difficulty getRandomDifficulty() {
        Difficulty[] values = Difficulty.values();
        return values[random.nextInt(values.length)];
    }

    private Person getRandomPerson() {
        return new Person(
                "person_%d".formatted(random.nextInt(10)),
                new Date(),
                random.nextLong(1, 100),
                getRandomLocation()
        );
    }

    private Location getRandomLocation() {
        return new Location(
                random.nextInt(-10, 10),
                random.nextFloat(-10, 10),
                "location_%d".formatted(random.nextInt(10))
        );
    }
}
