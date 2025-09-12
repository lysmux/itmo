package dev.lysmux.lab7.server;

import dev.lysmux.lab7.common.dto.Request;
import dev.lysmux.lab7.server.domain.User;
import lombok.Getter;

@Getter
public class RequestContext {
    private Request request;
    private User user;

    public void setContext(Request request, User user) {
        this.request = request;
        this.user = user;
    }
}
