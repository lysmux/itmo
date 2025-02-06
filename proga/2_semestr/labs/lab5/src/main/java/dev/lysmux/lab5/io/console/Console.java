package dev.lysmux.lab5.io.console;

/**
 * Interface for reading and writing data to and from the console
 *
 * @see AutoCloseable
 * @since 1.0
 */
public interface Console extends AutoCloseable {
    /**
     * Reads data from console. Block executing until data will be read
     *
     * @return string or {@code null} if no data available
     */
    String read();

    /**
     * Display prompt and reads data from console
     *
     * @param prompt prompt to display before reading
     * @return string or {@code null} if no data available
     * @see Console#read()
     */
    default String read(String prompt) {
        write(prompt);
        return read();
    }

    /**
     * Write string to console
     *
     * @param data string to write
     */
    void write(String data);

    /**
     * Write string to console and adds line break
     *
     * @param data string to write
     */
    default void writeln(String data) {
        write(data.concat(System.lineSeparator()));
    }
}
