package dev.lysmux.lab4.service;

import dev.lysmux.lab4.UserExistsException;
import dev.lysmux.lab4.domain.User;
import dev.lysmux.lab4.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService {
    @Inject
    private UserRepository userRepository;

    @Inject
    private AuthService authService;

    public User createUser(String username) {
        if (userRepository.isUserExists(username)) {
            throw new UserExistsException(username);
        }

        User user = new User(username);
        userRepository.addUser(user);
        return user;
    }

    public boolean isUserExists(String username) {
        return userRepository.isUserExists(username);
    }

    public User getUser(String userId) {
        return userRepository.getUserById(userId);
    }
}
