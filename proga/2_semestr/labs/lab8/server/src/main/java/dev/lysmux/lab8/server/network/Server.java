package dev.lysmux.lab8.server.network;

import dev.lysmux.lab8.common.command.CommandRegistry;
import dev.lysmux.lab8.common.command.wrapper.CommandWrapper;
import dev.lysmux.lab8.common.dto.Request;
import dev.lysmux.lab8.common.dto.Response;
import dev.lysmux.lab8.server.RequestContext;
import dev.lysmux.lab8.server.service.UserService;
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

    private final UserService userService;
    private final RequestContext requestContext;

    protected final CommandRegistry commandRegistry;
    protected final int listenPort;
    protected Selector selector;

    public Server(int port, CommandRegistry commandRegistry, UserService userService, RequestContext requestContext) {
        this.commandRegistry = commandRegistry;
        this.listenPort = port;
        this.userService = userService;
        this.requestContext = requestContext;
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

            if (!key.isValid()) continue;

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
            CommandWrapper commandWrapper = commandRegistry.getCommand(request.command());
            if (commandWrapper.requiresLogin()) {
                if (request.auth() == null) {
                    return Response.builder()
                            .success(false)
                            .text("Unauthorized access")
                            .build();
                }
                if (!userService.checkUserCredentials(
                        request.auth().login(),
                        request.auth().password()
                )) {
                    return Response.builder()
                            .success(false)
                            .text("Username or password is incorrect")
                            .build();
                }
                requestContext.setContext(request, userService.getUser(request.auth().login()));
            }

            return commandRegistry.getCommand(request.command()).execute(request.args());
        } catch (Exception e) {
            log.error("Failed to process client request", e);
            return Response.builder().text(e.getMessage()).success(false).build();
        }
    }
}
