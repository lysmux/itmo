package dev.lysmux.lab4.auth.providers.password.repository;

import dev.lysmux.lab4.auth.providers.password.model.PasswordUser;

public interface PasswordAuthRepository {
    void addUser(PasswordUser user);

    PasswordUser getUser(String username);
}
