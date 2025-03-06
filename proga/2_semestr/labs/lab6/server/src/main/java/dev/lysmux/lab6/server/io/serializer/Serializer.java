package dev.lysmux.lab6.server.io.serializer;


import dev.lysmux.lab6.common.collection.model.LabWork;
import dev.lysmux.lab6.server.io.serializer.exception.SerializeException;

import java.util.Collection;

/**
 * Interface for serialize collection to string data
 *
 * @since 1.0
 */
public interface Serializer {
    /**
     * Serialize collection to string
     *
     * @param collection collection to serialize
     * @return serialized to string collection
     * @throws SerializeException if collection could not be serialized
     */
    String serialize(Collection<LabWork> collection) throws SerializeException;

    /**
     * Deserialize collection from string data
     *
     * @param data string to deserialize
     * @return deserialized collection
     * @throws SerializeException if collection could not be deserialized
     */
    Collection<LabWork> deserialize(String data) throws SerializeException;
}
