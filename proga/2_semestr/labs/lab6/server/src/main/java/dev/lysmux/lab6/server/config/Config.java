package dev.lysmux.lab6.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Map;

/**
 * Class that stores server configuration
 *
 * @param collectionPath path to collection
 */
public record Config(
        int listenPort,
        ServerMode serverMode,
        Path collectionPath
) {
    private static final Logger log = LoggerFactory.getLogger(Config.class);

    /**
     * Default value for collection path
     */
    public static final String DEFAULT_COLLECTION_PATH = "data.xml";

    /**
     * Default value for server mode
     */
    public static final int DEFAULT_SERVER_PORT = 8000;

    /**
     * Default value for server mode
     */
    public static final ServerMode DEFAULT_SERVER_MODE = ServerMode.TCP;

    /**
     * Reads configuration from environment
     * <p>If parameter not exists, uses default value</p>
     *
     * @return new {@link Config} instance
     */
    public static Config fromEnv() {
        Map<String, String> env = System.getenv();

        int listenPort = parseIntOrDefault(env.get("LISTEN_PORT"), DEFAULT_SERVER_PORT);
        Path collectionPath = Path.of(env.getOrDefault("COLLECTION_PATH", DEFAULT_COLLECTION_PATH));
        ServerMode mode = parseEnumOrDefault(env.get("MODE"), ServerMode.class, DEFAULT_SERVER_MODE);

        return new Config(listenPort, mode, collectionPath);
    }

    private static int parseIntOrDefault(String value, int defaultValue) {
        try {
            return (value != null) ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            logParsingError(value, defaultValue);
            return defaultValue;
        }
    }

    private static <T extends Enum<T>> T parseEnumOrDefault(String value, Class<T> enumClass, T defaultValue) {
        try {
            return (value != null) ? Enum.valueOf(enumClass, value.toUpperCase()) : defaultValue;
        } catch (IllegalArgumentException e) {
            logParsingError(value, defaultValue);
            return defaultValue;
        }
    }

    private static void logParsingError(String value, Object defaultValue) {
        log.atWarn()
                .setMessage("Could not parse value. Using default")
                .addKeyValue("value", value)
                .addKeyValue("default", defaultValue)
                .log();
    }
}
