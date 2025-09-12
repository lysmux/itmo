package dev.lysmux.lab8.server.command.auth;

import dev.lysmux.lab8.common.command.Command;
import dev.lysmux.lab8.common.dto.ProfileData;
import dev.lysmux.lab8.common.dto.Response;
import dev.lysmux.lab8.server.RequestContext;
import dev.lysmux.lab8.server.service.UserService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Command that returns all registered commands and their descriptions
 *
 * @since 1.0
 */
@Command(name = "getProfileData", description = "Get profile data", requiresLogin = true)
@RequiredArgsConstructor
final public class GetProfileDataCommand {
    private final RequestContext requestContext;

    /**
     * Returns all registered commands and their descriptions
     *
     * @return execution result
     */
    public Response execute() {
        return Response.builder()
                .text("Profile data")
                .objects(List.of(new ProfileData(requestContext.getUser().id(), LocalDate.now(), LocalDate.now())))
                .build();
    }
}
