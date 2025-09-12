package dev.lysmux.lab6.common.io.file;

import java.io.IOException;

/**
 * Interface for interacting with files
 *
 * @since 1.0
 */
public interface FileManager {
    /**
     * Writes data to file
     *
     * @param filePath path to file
     * @param content  content to write
     * @throws IOException if data could not be writes
     */
    void write(String filePath, String content) throws IOException;

    /**
     * Reads data of file on this path
     *
     * @param filePath path to file
     * @return file content
     * @throws IOException if data could not be read
     */
    String read(String filePath) throws IOException;

    /**
     * Delete file on this path
     *
     * @param filePath path to file
     * @return {@code true} if file deleted else {@code false}
     */
    boolean delete(String filePath);

    /**
     * Checks if file on this path exists
     *
     * @param filePath path to file
     * @return {@code true} if exists else {@code false}
     */
    boolean exists(String filePath);
}
