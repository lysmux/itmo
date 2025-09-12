package dev.lysmux.lab6.server.io.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.lysmux.lab6.common.collection.model.LabWork;
import dev.lysmux.lab6.server.io.serializer.exception.SerializeException;

import java.util.Collection;

/**
 * XML Implementation of the {@link Serializer} interface.
 * <p>Uses XML format to serialize and deserialize collection</p>
 *
 * @see Serializer
 * @see <a href="https://en.wikipedia.org/wiki/XML">XML format</a>
 * @since 1.0
 */
final public class XMLSerializer implements Serializer {
    private final XmlMapper xmlMapper = XmlMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    /**
     * {@inheritDoc}
     */
    @Override
    public String serialize(Collection<LabWork> collection) throws SerializeException {
        try {
            return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(collection);
        } catch (JacksonException exc) {
            throw new SerializeException(exc);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<LabWork> deserialize(String data) throws SerializeException {
        try {
            return xmlMapper.readValue(data, new TypeReference<>() {
            });
        } catch (JacksonException exc) {
            throw new SerializeException(exc);
        }
    }
}
