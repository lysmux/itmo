package dev.lysmux.lab8.server.command.auth;

import dev.lysmux.lab8.common.command.Command;
import dev.lysmux.lab8.common.dto.Response;
import dev.lysmux.lab8.server.service.UserService;
import lombok.RequiredArgsConstructor;

/**
 * Command that returns all registered commands and their descriptions
 *
 * @since 1.0
 */
@Command(name = "login", description = "Login user")
@RequiredArgsConstructor
final public class LoginCommand {
    private final UserService userService;

    /**
     * Returns all registered commands and their descriptions
     *
     * @return execution result
     */
    public Response execute(String login, String password) {
        if (!userService.checkUserCredentials(login, password)) {
            return Response.builder()
                    .success(false)
                    .text("User with this login/password does not exist")
                    .build();
        }

        return Response.builder()
                .text("Successfully logged in")
                .build();
    }
}
