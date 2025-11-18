package dev.lysmux.lab4.repository.memory;

import dev.lysmux.lab4.domain.model.User;
import dev.lysmux.lab4.domain.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Alternative
public class MemoryUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();

    public User addUser(User user) {
        users.add(user);
        return user;
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
