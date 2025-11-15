package dev.lysmux.lab4.service;

import dev.lysmux.lab4.InvalidCredentialsException;
import dev.lysmux.lab4.UserExistsException;
import dev.lysmux.lab4.domain.TokensPair;
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

    public TokensPair register(String username, String password) {
        if (userRepository.isUserExists(username)) {
            throw new UserExistsException(username);
        }

        User user = new User(username, password);
        userRepository.addUser(user);
        return authService.generateTokensPair(user.id());
    }

    public TokensPair login(String username, String password) {
        User user = userRepository.getUserByName(username);
        if (user == null || !user.password().equals(password)) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
        return authService.generateTokensPair(user.id());
    }

    public boolean isUserExists(String username) {
        return userRepository.isUserExists(username);
    }

    public User getUser(String userId) {
        return userRepository.getUserById(userId);
    }
}
