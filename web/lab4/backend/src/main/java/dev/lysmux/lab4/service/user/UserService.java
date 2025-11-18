package dev.lysmux.lab4.service.user;

import dev.lysmux.lab4.domain.model.User;
import dev.lysmux.lab4.domain.repository.UserRepository;
import dev.lysmux.lab4.service.user.exception.UserExistsException;
import dev.lysmux.lab4.service.user.exception.UserNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService {
    @Inject
    private UserRepository userRepository;

    public User createUser(String username) {
        if (userRepository.isUserExists(username)) {
            throw new UserExistsException(username);
        }

        User user = new User(username);
        return userRepository.addUser(user);
    }

    public boolean isUserExists(String username) {
        return userRepository.isUserExists(username);
    }

    public User getUserById(String userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw UserNotFoundException.byId(userId);
        }

        return user;
    }

    public User getUserByUsername(String username) {
        User user = userRepository.getUserByName(username);
        if (user == null) {
            throw UserNotFoundException.byUsername(username);
        }

        return user;
    }
}
