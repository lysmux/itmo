package dev.lysmux.lab4.repository;

import dev.lysmux.lab4.domain.User;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UserRepository {
    private final List<User> users = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
    }

    public User getUserById(String userId) {
        return users.stream()
                .filter(user -> user.id().equals(userId))
                .findFirst()
                .orElse(null);
    }

    public User getUserByName(String username) {
        return users.stream()
                .filter(user -> user.username().equals(username))
                .findFirst()
                .orElse(null);
    }

    public boolean isUserExists(String username) {
        return users.stream()
                .anyMatch(user -> user.username().equals(username));
    }
}
