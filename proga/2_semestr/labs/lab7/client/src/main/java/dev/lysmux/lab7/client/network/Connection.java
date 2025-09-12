package dev.lysmux.lab7.client.network;

import java.io.IOException;

/**
 * Interface for interacting with connection
 *
 * @since 1.0
 */
public interface Connection extends AutoCloseable {
    /**
     * Read data from server
     *
     * @return read data
     * @throws IOException if I/O error occurs
     */
    byte[] read() throws IOException;

    /**
     * Sent data to server
     *
     * @param data data to send
     * @throws IOException if I/O error occurs
     */
    void send(byte[] data) throws IOException;
}
