package dev.lysmux.lab7.client.controller.command.auth;

import dev.lysmux.lab7.client.AuthContainer;
import dev.lysmux.lab7.client.network.Client;
import dev.lysmux.lab7.common.command.Command;
import dev.lysmux.lab7.common.dto.Request;
import dev.lysmux.lab7.common.dto.Response;
import lombok.RequiredArgsConstructor;

/**
 * Command that returns all registered commands and their descriptions
 *
 * @since 1.0
 */
@Command(name = "login", description = "Login user")
@RequiredArgsConstructor
final public class LoginCommand {
    private final AuthContainer authContainer;
    private final Client client;

    /**
     * Returns all registered commands and their descriptions
     *
     * @return execution result
     */
    public Response execute(String login, String password) {
        Response response = client.request(new Request("login", new Object[]{login, password}));
        if (response.success()) {
            authContainer.setAuth(login, password);
        }
        return response;
    }
}
