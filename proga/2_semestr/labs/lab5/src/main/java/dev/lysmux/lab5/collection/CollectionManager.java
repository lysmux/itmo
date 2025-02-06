package dev.lysmux.lab5.collection;

import dev.lysmux.lab5.collection.model.LabWork;
import dev.lysmux.lab5.io.file.FileManager;
import dev.lysmux.lab5.io.file.StandartFileManager;
import dev.lysmux.lab5.io.serializer.Serializer;
import dev.lysmux.lab5.io.serializer.XMLSerializer;
import dev.lysmux.lab5.io.serializer.exception.SerializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * CLass for managing collection
 *
 * @since 1.0
 */
public class CollectionManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(CollectionManager.class);

    private final Path collectionPath;
    private final Path backupPath;

    private final FileManager fileManager = new StandartFileManager();
    private final Serializer serializer = new XMLSerializer();

    private boolean isSaved = true;

    private final TreeSet<LabWork> collection = new TreeSet<>();

    /**
     * Creates new {@link CollectionManager}
     *
     * @param collectionPath path to collection
     */
    public CollectionManager(Path collectionPath) {
        this.collectionPath = collectionPath.toAbsolutePath();

        Path parentDir = this.collectionPath.getParent();
        backupPath = parentDir.resolve("." + collectionPath.getFileName());
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
    public void add(LabWork entity) {
        entity.setId(getNextEntityId());

        collection.add(entity);
        handleCollectionUpdate();
    }

    /**
     * Removes entity from collection by id
     *
     * @param id id of entity to remove
     */
    public void remove(int id) {
        if (collection.removeIf(el -> el.getId() == id)) {
            handleCollectionUpdate();
        }
    }

    /**
     * Updates collection entity
     *
     * @param entity entity to update
     */
    public void update(LabWork entity) {
        remove(entity.getId());
        collection.add(entity);
        handleCollectionUpdate();
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


    /**
     * Clears collection
     */
    public void clear() {
        collection.clear();
        handleCollectionUpdate();
    }

    /**
     * Returns information about collection
     *
     * @return collection information
     */
    public CollectionMeta getCollectionMeta() {
        return new CollectionMeta(collection.getClass().getName(), collection.size(), collectionPath.toString());
    }

    /**
     * Saves collection to file
     */
    public void save() {
        try {
            save(collectionPath);
            deleteBackup();
            isSaved = true;
        } catch (SerializeException | IOException e) {
            LOGGER.error("Failed to save collection");
        }
    }

    /**
     * Loads collection from file
     */
    public void load() {
        try {
            load(collectionPath);
        } catch (SerializeException | IOException e) {
            LOGGER.error("Failed to load collection");
        }
    }

    /**
     * Loads collection from specify file
     *
     * @param path path to load
     * @throws SerializeException if collection could not be deserialized from string
     * @throws IOException        if collection could not be read
     */
    private void load(Path path) throws SerializeException, IOException {
        String fileContent = fileManager.read(path.toString());
        Collection<LabWork> loaded = serializer.deserialize(fileContent);
        Set<Integer> existingIds = new HashSet<>();

        collection.clear();
        for (LabWork entity : loaded) {
            if (!existingIds.add(entity.getId())) {
                int newId = getNextEntityId();
                entity.setId(newId);
                LOGGER.warn("Entity `{}` has non-unique ID. New ID: {}", entity.getName(), newId);
            }
            collection.add(entity);
        }
    }

    /**
     * Saves collection to specify file
     *
     * @param path path to save
     * @throws SerializeException if collection could not be serialized to string
     * @throws IOException        if collection could not be written
     */
    private void save(Path path) throws SerializeException, IOException {
        String data = serializer.serialize(collection);
        fileManager.write(path.toString(), data);
    }

    /**
     * Backups collection
     */
    public void saveBackup() {
        try {
            save(backupPath);
        } catch (SerializeException | IOException e) {
            LOGGER.error("Failed to save backup");
        }
    }

    /**
     * Restores collection
     */
    public void restoreBackup() {
        try {
            load(backupPath);
            deleteBackup();
        } catch (SerializeException | IOException e) {
            LOGGER.error("Failed to restore backup");
        }
    }

    /**
     * Deletes backup
     */
    public void deleteBackup() {
        fileManager.delete(backupPath.toString());
    }

    /**
     * Checks if collection has backup
     *
     * @return {@code true} if collection has backup else {@code false}
     */
    public boolean backupExists() {
        return fileManager.exists(backupPath.toString());
    }

    /**
     * Checks if collection is saved
     *
     * @return {@code true} if collection is saved else {@code false}
     */
    public boolean isCollectionSaved() {
        return isSaved;
    }

    /**
     * Executes every time when collection updates
     */
    private void handleCollectionUpdate() {
        isSaved = false;
        saveBackup();
    }

    private int getNextEntityId() {
        if (collection.isEmpty()) return 1;
        return collection.stream().map(LabWork::getId).max(Integer::compareTo).orElse(0) + 1;
    }
}
