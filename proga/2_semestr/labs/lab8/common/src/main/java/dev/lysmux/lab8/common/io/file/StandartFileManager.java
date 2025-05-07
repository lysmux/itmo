package dev.lysmux.lab8.common.io.file;

import java.io.*;

/**
 * Implementation of the {@link FileManager} interface.
 *
 * <p>
 * This class uses {@link BufferedInputStream} for read data
 * and {@link PrintWriter} for write data from and to file
 * </p>
 *
 * @see FileManager
 * @see BufferedInputStream
 * @see PrintWriter
 */
final public class StandartFileManager implements FileManager {
    /**
     * {@inheritDoc}
     * <p>Uses {@link PrintWriter} for write content to file</p>
     *
     * @see PrintWriter
     */
    @Override
    public void write(String filePath, String content) throws IOException {
        var writer = new PrintWriter(new FileOutputStream(filePath));
        writer.write(content);
        writer.close();
    }

    /**
     * {@inheritDoc}
     * <p>Uses {@link BufferedInputStream} for read file content</p>
     *
     * @see BufferedInputStream
     */
    @Override
    public String read(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        var reader = new BufferedInputStream(new FileInputStream(filePath));

        int data;
        while ((data = reader.read()) != -1) {
            sb.append((char) data);
        }
        reader.close();

        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }
}
