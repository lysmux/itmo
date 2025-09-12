package dev.lysmux.lab7.client;

import lombok.Getter;

@Getter
public class AuthContainer {
    private String login;
    private String password;

    public void setAuth(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public boolean isSet() {
        return login != null && password != null;
    }
}
