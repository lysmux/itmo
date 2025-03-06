package dev.lysmux.lab6.server.network;

import dev.lysmux.lab6.common.command.CommandRegistry;
import dev.lysmux.lab6.common.dto.Request;
import dev.lysmux.lab6.common.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * Abstract server class
 *
 * @since 1.0
 */
public abstract class Server implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Server.class);

    protected final CommandRegistry commandRegistry;
    protected final int listenPort;
    protected Selector selector;

    public Server(int port, CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
        this.listenPort = port;
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    /**
     * Server main loop
     */
    public void run() {
        try {
            selector = Selector.open();
            initServer();
        } catch (IOException e) {
            log.atError()
                    .setMessage("Failed to start server")
                    .setCause(e)
                    .log();
            System.exit(1);
        }

        try {
            while (selector.isOpen()) processSelector();
        } catch (IOException e) {
            log.atError()
                    .setMessage("Error in server main loop")
                    .setCause(e)
                    .log();
        }
    }

    /**
     * Initializes server
     *
     * @throws IOException if i/o error occurs
     */
    protected abstract void initServer() throws IOException;

    /**
     * Closes server
     *
     * @throws IOException if i/o error occurs
     */
    protected abstract void closeServer() throws IOException;

    /**
     * Closes selector and server
     */
    public void close() {
        log.info("Shutting down server...");
        try {
            if (selector != null) selector.close();
            closeServer();
            log.info("Server stopped");
        } catch (IOException e) {
            log.atError()
                    .setMessage("Error occurred while shutting down server")
                    .setCause(e)
                    .log();
        }
    }

    /**
     * Select keys and process them
     *
     * @throws IOException if i/o error occurs
     */
    private void processSelector() throws IOException {
        if (selector.selectNow() == 0) return;
        Iterator<SelectionKey> iter = selector.selectedKeys().iterator();

        while (iter.hasNext()) {
            SelectionKey key = iter.next();
            iter.remove();
            handleKey(key);
        }
    }

    /**
     * Handle selector key
     *
     * @param key to handle
     */
    protected abstract void handleKey(SelectionKey key);

    /**
     * @param request request to handle
     * @return execution response
     */
    protected Response handleClientRequest(Request request) {
        try {
            return commandRegistry.getCommand(request.command()).execute(request.args());
        } catch (Exception e) {
            log.error("Failed to process client request", e);
            return Response.builder().text(e.getMessage()).success(false).build();
        }
    }
}
