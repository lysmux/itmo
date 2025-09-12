package dev.lysmux.lab5.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Map;

/**
 * Class that stores program configuration
 *
 * @param collectionPath path to collection
 */
public record Config(Path collectionPath) {
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    /**
     * Default value for collection path
     */
    public static final String DEFAULT_COLLECTION_PATH = "data.xml";

    /**
     * Reads configuration from environment
     * <p>If parameter not exists, uses default value</p>
     *
     * @return new {@link Config} instance
     */
    public static Config fromEnv() {
        Map<String, String> env = System.getenv();
        Path collectionPath = Path.of(env.getOrDefault("COLLECTION_PATH", DEFAULT_COLLECTION_PATH));

        return new Config(collectionPath);
    }

    private static int parseIntOrDefault(String value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOGGER.warn("Could not parse value '{}'. Using default: {}", value, defaultValue);
            return defaultValue;
        }
    }
}
