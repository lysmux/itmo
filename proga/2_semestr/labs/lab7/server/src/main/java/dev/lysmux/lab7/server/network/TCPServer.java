package dev.lysmux.lab7.server.network;

import dev.lysmux.lab7.server.RequestContext;
import dev.lysmux.lab7.common.command.CommandRegistry;
import dev.lysmux.lab7.common.dto.Request;
import dev.lysmux.lab7.common.dto.Response;
import dev.lysmux.lab7.server.service.UserService;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * TCP server implementation
 *
 * @since 1.0
 */
public class TCPServer extends Server {
    private static final Logger log = LoggerFactory.getLogger(TCPServer.class);

    private ServerSocketChannel serverSocketChannel;
    protected final Map<SelectableChannel, Queue<Response>> clients = new ConcurrentHashMap<>();

    private final ExecutorService readPool = getPool();
    private final ExecutorService executePool = getPool();
    private final ExecutorService writePool = getPool();

    // очередь на прием, обработку, отправку

    public TCPServer(int port, CommandRegistry commandRegistry, UserService userService, RequestContext requestContext) {
        super(port, commandRegistry, userService, requestContext);
    }

    private ExecutorService getPool() {
        return new ForkJoinPool(10);
    }

    /**
     * Initializes server
     *
     * @throws IOException if i/o error occurs
     */
    @Override
    protected void initServer() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(listenPort));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        log.info("TCP Server started on port: {}", listenPort);
    }

    /**
     * Handle selector key
     *
     * @param key to handle
     */
    @Override
    protected void handleKey(SelectionKey key) {
        try {
            if (key.isAcceptable()) {
                acceptClient();
            } else if (key.isReadable()) {
                key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
                readPool.execute(() -> {
                    try {
                        handleRead(key);
                    } catch (IOException e) {
                        key.cancel();
                        closeClient(key.channel());
                    }
                });
            } else if (key.isWritable()) {
                key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
                writePool.execute(() -> {
                    try {
                        handleWrite(key);
                    } catch (IOException e) {
                        key.cancel();
                        closeClient(key.channel());
                    }
                });
            }
        } catch (IOException e) {
            key.cancel();
            closeClient(key.channel());

            log.atInfo()
                    .setMessage("Client disconnected")
                    .addKeyValue("client", key.channel())
                    .log();
        }

    }

    /**
     * Accept new client
     *
     * @throws IOException if i/o error occurs
     */
    private void acceptClient() throws IOException {
        SocketChannel client = serverSocketChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        clients.put(client, new ConcurrentLinkedQueue<>());

        log.atInfo()
                .setMessage("Client connected")
                .addKeyValue("address", client.getRemoteAddress())
                .log();
    }

    /**
     * Reads from client
     *
     * @param key client key
     * @throws IOException if i/o error occurs
     */
    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
        client.read(sizeBuffer);
        sizeBuffer.flip();

        ByteBuffer dataBuffer = ByteBuffer.allocate(sizeBuffer.getInt());
        while (dataBuffer.hasRemaining()) {client.read(dataBuffer);}
        dataBuffer.flip();

        Request request = SerializationUtils.deserialize(dataBuffer.array());
        Response response = handleClientRequest(request);
        clients.get(client).add(response);
        key.interestOps(SelectionKey.OP_WRITE);

        log.atInfo()
                .setMessage("New request from client")
                .addKeyValue("address", client.getRemoteAddress())
                .addKeyValue("size", dataBuffer.capacity())
                .log();
    }

    /**
     * Write to client
     *
     * @param key client key
     * @throws IOException if i/o error occurs
     */
    private void handleWrite(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        Queue<Response> queue = clients.get(client);
        if (queue == null) return;

        while (!queue.isEmpty()) {
            byte[] data = SerializationUtils.serialize(queue.remove());
            ByteBuffer buffer = ByteBuffer.allocate(4 + data.length);
            buffer.putInt(data.length);
            buffer.put(data);
            buffer.flip();

            while (buffer.hasRemaining()) {
                client.write(buffer);
            }

            log.atInfo()
                    .setMessage("Data sent to client")
                    .addKeyValue("address", client.getRemoteAddress())
                    .addKeyValue("size", buffer.capacity())
                    .log();
        }
        key.interestOps(SelectionKey.OP_READ);
    }

    /**
     * Closes client
     *
     * @param channel client
     */
    private void closeClient(SelectableChannel channel) {
        clients.remove(channel);
        try {
            channel.close();
        } catch (IOException e) {
            log.atError()
                    .setMessage("Failed to close client")
                    .addKeyValue("channel", channel)
                    .setCause(e)
                    .log();
        }
    }

    /**
     * Closes server
     *
     * @throws IOException if i/o error occurs
     */
    @Override
    protected void closeServer() throws IOException {
        if (serverSocketChannel != null) serverSocketChannel.close();
        clients.keySet().forEach(this::closeClient);

        readPool.shutdown();
        writePool.shutdown();
        executePool.shutdown();
    }
}
