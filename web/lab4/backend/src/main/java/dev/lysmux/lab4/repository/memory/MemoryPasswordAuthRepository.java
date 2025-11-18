package dev.lysmux.lab4.repository.memory;

import dev.lysmux.lab4.auth.providers.password.model.PasswordUser;
import dev.lysmux.lab4.auth.providers.password.repository.PasswordAuthRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MemoryPasswordAuthRepository implements PasswordAuthRepository {
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
