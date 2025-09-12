package dev.lysmux.lab7.client.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * Connection that uses UDP protocol
 *
 * @since 1.0
 */
public class UDPConnection implements Connection {
    private static final int BUFFER_SIZE = 1024;

    private final DatagramChannel channel;
    private final InetSocketAddress serverAddress;

    public UDPConnection(String host, int port) throws IOException {
        serverAddress = new InetSocketAddress(host, port);
        channel = DatagramChannel.open();
    }

    /**
     * Close UDP connection
     *
     * @throws Exception if i/o error occurs
     */
    @Override
    public void close() throws Exception {
        channel.close();
    }

    /**
     * Read data from server UDP socket
     *
     * @return read data
     * @throws IOException if i/o error occurs
     */
    public byte[] read() throws IOException {
        Map<Integer, byte[]> receivedPackets = new HashMap<>();
        byte[] receivedData;

        while (true) {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            InetSocketAddress sender = (InetSocketAddress) channel.receive(buffer);
            if (sender != null && sender.equals(serverAddress)) {
                buffer.flip();
                int seqNum = buffer.get();
                int totalPackets = buffer.getInt();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);
                receivedPackets.put(seqNum, data);

//                ByteBuffer ackBuffer = ByteBuffer.allocate(1).put((byte) seqNum);
//                ackBuffer.flip();
//                channel.send(ackBuffer, serverAddress);
                if (receivedPackets.size() == totalPackets) {
                    receivedData = assembleMessage(receivedPackets);
                    return receivedData;
                }
            }
        }
    }

    /**
     * Construct message from received packets
     *
     * @param packets received packets
     * @return message
     */
    private static byte[] assembleMessage(Map<Integer, byte[]> packets) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        packets.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    try {
                        outputStream.write(entry.getValue());
                    } catch (IOException ignored) {
                    }
                });

        return outputStream.toByteArray();
    }

    /**
     * Sent data to server UDP socket
     *
     * @param data data to send
     * @throws IOException if I/O error occurs
     */
    public void send(byte[] data) throws IOException {
        final int PACKET_SIZE = BUFFER_SIZE - 5;
        int numPackets = (int) Math.ceil(data.length / (double) PACKET_SIZE);

        for (int i = 0; i < numPackets; i++) {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            buffer.put((byte) i);
            buffer.putInt(numPackets);
            buffer.put(data, i * PACKET_SIZE, Math.min(PACKET_SIZE, data.length - i * PACKET_SIZE));
            buffer.flip();
            channel.send(buffer, serverAddress);

//            boolean ackReceived = false;
//            while (!ackReceived) {
//                channel.send(buffer, serverAddress);
//                buffer.rewind();
//
//                ByteBuffer ackBuffer = ByteBuffer.allocate(1);
//                InetSocketAddress sender = (InetSocketAddress) channel.receive(ackBuffer);
//                if (sender != null && sender.equals(serverAddress)) {
//                    ackBuffer.flip();
//                    if (ackBuffer.get() == (byte) i) {
//                        ackReceived = true;
//                    }
//                }
//            }
        }
    }
}
