package dev.lysmux.lab8.server.collection;


import dev.lysmux.lab8.common.collection.model.LabWork;
import dev.lysmux.lab8.server.repository.LabWorkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * Class for managing collection
 *
 * @since 1.0
 */
public class CollectionManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(CollectionManager.class);

    private final TreeSet<LabWork> collection = new TreeSet<>();

    private final LabWorkRepository labWorkRepository;

    public CollectionManager(LabWorkRepository labWorkRepository) {
        this.labWorkRepository = labWorkRepository;
    }

    /**
     * Gets collection
     *
     * @return unmodifiable collection
     */
    public Set<LabWork> getCollection() {
        return Collections.unmodifiableSortedSet(collection);
    }

    /**
     * Add new entity to collection
     *
     * @param entity entity to add
     */
    synchronized public void add(LabWork entity) {
        int id = labWorkRepository.add(entity);

        entity.setId(id);
        collection.add(entity);
    }

    /**
     * Removes entity from collection by id
     *
     * @param id id of entity to remove
     */
    synchronized public void remove(int id) {
        if (collection.removeIf(el -> el.getId() == id)) labWorkRepository.remove(id);;
    }

    /**
     * Updates collection entity
     *
     * @param entity entity to update
     */
    synchronized public void update(LabWork entity) {
        collection.remove(entity);
        collection.add(entity);
    }

    /**
     * Checks if entity with provided id exists in collection
     *
     * @param id id of entity to check
     * @return {@code true} if entity exists else {@code false}
     */
    public boolean exists(int id) {
        return collection.stream().anyMatch(el -> el.getId() == id);
    }


    public boolean hasAccess(int id, int userID) {
        return collection.stream().anyMatch(el -> el.getId() == id && el.getOwnerId() == userID);
    }

    /**
     * Clears collection
     */
    synchronized public void clear(int ownerID) {
        if (collection.removeIf(el -> el.getOwnerId() == ownerID)) labWorkRepository.removeByOwnerID(ownerID);
    }

    /**
     * Returns information about collection
     *
     * @return collection information
     */
    public CollectionMeta getCollectionMeta() {
        return new CollectionMeta(collection.getClass().getName(), collection.size());
    }


    public void load() {
        collection.clear();
        collection.addAll(labWorkRepository.getAll());
    }
}
