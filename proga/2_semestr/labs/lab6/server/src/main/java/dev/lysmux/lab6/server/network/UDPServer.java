package dev.lysmux.lab6.server.network;

import dev.lysmux.lab6.common.command.CommandRegistry;
import dev.lysmux.lab6.common.dto.Request;
import dev.lysmux.lab6.common.dto.Response;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;

public class UDPServer extends Server {
    private static final Logger log = LoggerFactory.getLogger(UDPServer.class);

    private static final int BUFFER_SIZE = 1024;
    private static final int PACKET_SIZE = BUFFER_SIZE - 5;

    private DatagramChannel channel;
    private static final Map<InetSocketAddress, Map<Integer, byte[]>> clientMessages = new HashMap<>();

    public UDPServer(int port, CommandRegistry commandRegistry) {
        super(port, commandRegistry);
    }

    /**
     * Initializes server
     *
     * @throws IOException if i/o error occurs
     */
    @Override
    protected void initServer() throws IOException {
        channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(listenPort));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        log.info("UDP Server started on port: {}", listenPort);
    }

    /**
     * Handle selector key
     *
     * @param key to handle
     */
    @Override
    protected void handleKey(SelectionKey key) {
        try {
            if (key.isReadable()) handleRead();
        } catch (IOException e) {
            key.cancel();
        }
    }

    /**
     * Reads from client
     *
     * @throws IOException if i/o error occurs
     */
    private void handleRead() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        InetSocketAddress clientAddress = (InetSocketAddress) channel.receive(buffer);
        if (clientAddress == null) return;
        buffer.flip();

        int seqNum = buffer.get();
        int totalPackets = buffer.getInt();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        clientMessages.computeIfAbsent(clientAddress, k -> new HashMap<>()).put(seqNum, data);

        if (clientMessages.get(clientAddress).size() == totalPackets) {
            byte[] message = assembleMessage(clientMessages.remove(clientAddress));

            log.atInfo()
                    .setMessage("New request from client")
                    .addKeyValue("address", clientAddress)
                    .addKeyValue("size", message.length)
                    .log();

            Request request = SerializationUtils.deserialize(message);
            Response response = handleClientRequest(request);
            sendResponse(response, clientAddress);
        }
    }

    /**
     * Construct message from received packets
     *
     * @param packets received packets
     * @return message
     */
    private byte[] assembleMessage(Map<Integer, byte[]> packets) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        packets.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    try {
                        baos.write(entry.getValue());
                    } catch (IOException ignored) {
                    }
                });
        return baos.toByteArray();
    }

    /**
     * Sends response to client
     *
     * @param response      response to send
     * @param clientAddress client address
     * @throws IOException if i/o error occurs
     */
    private void sendResponse(Response response, InetSocketAddress clientAddress) throws IOException {
        byte[] data = SerializationUtils.serialize(response);
        int numPackets = (int) Math.ceil((double) data.length / (PACKET_SIZE));
        for (int i = 0; i < numPackets; i++) {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            buffer.put((byte) i);
            buffer.putInt(numPackets);
            buffer.put(data, i * PACKET_SIZE, Math.min(PACKET_SIZE, data.length - i * PACKET_SIZE));
            buffer.flip();
            channel.send(buffer, clientAddress);

            log.atInfo()
                    .setMessage("Data sent to client")
                    .addKeyValue("address", clientAddress)
                    .addKeyValue("size", buffer.capacity())
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
        if (channel != null) channel.close();
    }
}
