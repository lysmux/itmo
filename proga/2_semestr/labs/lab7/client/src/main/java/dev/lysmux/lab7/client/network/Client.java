package dev.lysmux.lab7.client.network;

import dev.lysmux.lab7.client.config.ClientMode;
import dev.lysmux.lab7.common.command.meta.CommandInfo;
import dev.lysmux.lab7.common.dto.Request;
import dev.lysmux.lab7.common.dto.Response;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Wrapper to interact with server
 *
 * @since 1.0
 */
public class Client implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(Client.class);

    private Connection connection;

    private final String host;
    private final int port;
    private final ConnectionFactory connectionFactory;

    public Client(String host, int port, ClientMode mode) {
        this.host = host;
        this.port = port;
        connectionFactory = new ConnectionFactory(host, port, mode);
        connect();
    }

    /**
     * Connect to server
     */
    public void connect() {
        int attempts = 0;

        while (true) {
            try {
                connection = connectionFactory.create();

                log.atInfo()
                        .setMessage("Connected to server")
                        .addKeyValue("host", host)
                        .addKeyValue("port", port)
                        .log();
                break;
            } catch (IOException ex) {
                log.atError()
                        .setMessage("Failed to connect to server. Retrying after 5 seconds")
                        .addKeyValue("attempt", ++attempts)
                        .addKeyValue("host", host)
                        .addKeyValue("port", port)
                        .addKeyValue("error", ex.getMessage())
                        .log();
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    /**
     * Close connection
     *
     * @throws Exception if
     */
    @Override
    public void close() throws Exception {
        if (connection != null) connection.close();
    }

    /**
     * Request data from server
     *
     * @param request data to request
     * @return server response
     */
    public Response request(Request request) {
        try {
            connection.send(SerializationUtils.serialize(request));
            return SerializationUtils.deserialize(connection.read());
        } catch (IOException e) {
            log.atError()
                    .setMessage("Lost connection to server")
                    .log();
            connect();
            return request(request);
        }
    }

    /**
     * Fetch commands from server
     *
     * @return server commands
     */
    public List<CommandInfo> fetchServerCommands() {
        Request request = new Request("get_commands");
        Response response = request(request);

        @SuppressWarnings("unchecked")
        List<CommandInfo> commands = (List<CommandInfo>) response.objects();
        return commands;
    }
}
