package dev.lysmux.lab7.client.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Class that stores client configuration
 *
 * @param host       server host
 * @param port       server port
 * @param clientMode client mode
 */
public record Config(
        String host,
        int port,
        ClientMode clientMode
) {
    private static final Logger log = LoggerFactory.getLogger(Config.class);

    public static final String DEFAULT_SERVER_HOST = "localhost";
    public static final int DEFAULT_SERVER_PORT = 8000;
    public static final ClientMode DEFAULT_CLIENT_MODE = ClientMode.TCP;


    /**
     * Read config from app arguments
     *
     * @return config
     */
    public static Config fromArgs() {
        Properties properties = System.getProperties();

        String host = properties.getProperty("host", DEFAULT_SERVER_HOST);
        int port = parseIntOrDefault(properties.getProperty("port"), DEFAULT_SERVER_PORT);
        ClientMode clientMode = parseEnumOrDefault(properties.getProperty("mode"), ClientMode.class, DEFAULT_CLIENT_MODE);

        return new Config(host, port, clientMode);
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
