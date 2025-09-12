package dev.lysmux.lab7.server.command.auth;

import dev.lysmux.lab7.common.command.Command;
import dev.lysmux.lab7.common.dto.Response;
import dev.lysmux.lab7.server.service.UserService;
import lombok.RequiredArgsConstructor;

/**
 * Command that returns all registered commands and their descriptions
 *
 * @since 1.0
 */
@Command(name = "register", description = "Register user")
@RequiredArgsConstructor
final public class RegisterCommand {
    private final UserService userService;

    /**
     * Returns all registered commands and their descriptions
     *
     * @return execution result
     */
    public Response execute(String login, String password) {
        if (!userService.registerUser(login, password)) {
            return Response.builder()
                    .success(false)
                    .text("User already exists")
                    .build();
        }
        return Response.builder()
                .text("User %s registered".formatted(login))
                .build();
    }
}
