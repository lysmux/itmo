package dev.lysmux.lab4.domain.repository;

import dev.lysmux.lab4.domain.model.User;

public interface UserRepository {
    User addUser(User user);

    User getUserById(String userId);

    User getUserByName(String username);

    boolean isUserExists(String username);
}
