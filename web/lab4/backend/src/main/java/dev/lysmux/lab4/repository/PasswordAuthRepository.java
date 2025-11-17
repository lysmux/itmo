package dev.lysmux.lab4.repository;

import dev.lysmux.lab4.auth.providers.password.PasswordUser;
import dev.lysmux.lab4.auth.providers.vk.VKUser;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PasswordAuthRepository {
    private final List<PasswordUser> users = new ArrayList<>();

    public void addUser(PasswordUser user) {
        users.add(user);
    }

    public PasswordUser getUser(String username) {
        return users.stream()
                .filter(user -> user.username().equals(username))
                .findFirst()
                .orElse(null);
    }
}
