package dev.lysmux.lab5.io.console;

import java.io.*;

/**
 * Buffered implementation of the {@link Console} interface.
 * <p>
 * This class provides buffered input and output functionality for the console using
 * {@link BufferedReader} and {@link BufferedWriter}. It supports reading from and
 * writing to the console with efficient buffering.
 * </p>
 *
 * @see Console
 * @see BufferedReader
 * @see BufferedWriter
 * @since 1.0
 */
final public class StandartConsole implements Console {
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

    /**
     * {@inheritDoc}
     * <p>Write to the console buffer and flush immediately</p>
     */
    @Override
    public void write(String data) {
        try {
            writer.write(data);
            writer.flush();
        } catch (IOException ignored) {
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return string or {@code null} if I/O errors occurs
     */
    @Override
    public String read() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Closes reader and writer streams.
     * <p>After this read and write operations not available</p>
     *
     * @throws IOException if console can not be closed
     */
    @Override
    public void close() throws IOException {
        reader.close();
        writer.close();
    }
}
