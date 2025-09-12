package dev.lysmux.lab8.client.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Connection that uses TCP protocol
 *
 * @since 1.0
 */
public class TCPConnection implements Connection {
    private final Socket socket;
    private final BufferedInputStream inputStream;
    private final BufferedOutputStream outputStream;

    public TCPConnection(String host, int port) throws IOException {
        InetSocketAddress address = new InetSocketAddress(host, port);

        socket = new Socket();
        socket.setSoTimeout(30000);
        socket.connect(address, 3000);

        inputStream = new BufferedInputStream(socket.getInputStream());
        outputStream = new BufferedOutputStream(socket.getOutputStream());
    }

    /**
     * Close TCP connection
     *
     * @throws Exception if i/o error occurs
     */
    @Override
    public void close() throws Exception {
        socket.close();
    }

    /**
     * Read data from server TCP socket
     *
     * @return read data
     * @throws IOException if i/o error occurs
     */
    public byte[] read() throws IOException {
        byte[] inBuff = inputStream.readNBytes(4);
        if (inBuff.length == 0) throw new IOException("No data received");

        ByteBuffer sizeBuffer = ByteBuffer.wrap(inBuff);
        return inputStream.readNBytes(sizeBuffer.getInt());
    }


    /**
     * Sent data to server TCP socket
     *
     * @param data data to send
     * @throws IOException if I/O error occurs
     */
    public void send(byte[] data) throws IOException {
        ByteBuffer dataBuffer = ByteBuffer.allocate(4 + data.length);
        dataBuffer.putInt(data.length);
        dataBuffer.put(data);
        dataBuffer.flip();

        outputStream.write(dataBuffer.array());
        outputStream.flush();
    }
}
