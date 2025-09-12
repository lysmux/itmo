package dev.lysmux.lab6.client.network;

import dev.lysmux.lab6.client.config.ClientMode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Connection factory by mode
 *
 * @since 1.0
 */
@RequiredArgsConstructor
public class ConnectionFactory {
    private static final Logger log = LoggerFactory.getLogger(ConnectionFactory.class);

    private final String host;
    private final int port;
    private final ClientMode mode;

    /**
     * Creates new connection instance by mode
     *
     * @return new connection instance
     * @throws IOException if connection could not be created
     */
    public Connection create() throws IOException {
        if (mode == ClientMode.TCP) {
            log.info("Creating TCP connection");

            return new TCPConnection(host, port);
        } else {
            log.info("Creating UDP connection");

            return new UDPConnection(host, port);
        }
    }
}
