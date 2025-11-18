package dev.lysmux.lab4.auth.providers.password;

import dev.lysmux.lab4.auth.providers.password.exception.InvalidCredentialsException;
import dev.lysmux.lab4.auth.providers.password.model.PasswordCredentials;
import dev.lysmux.lab4.auth.providers.password.model.PasswordUser;
import dev.lysmux.lab4.auth.providers.password.repository.PasswordAuthRepository;
import dev.lysmux.lab4.domain.model.User;
import dev.lysmux.lab4.service.user.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PasswordAuthProvider {
    @Inject
    private UserService userService;

    @Inject
    private PasswordAuthRepository repository;

    public User register(PasswordCredentials credentials) {
        User user = userService.createUser(credentials.username());
        repository.addUser(new PasswordUser(user.id(), credentials.username(), credentials.password()));

        return user;
    }

    public User login(PasswordCredentials credentials) {
        PasswordUser passwordUser = repository.getUser(credentials.username());

        if (passwordUser != null && passwordUser.password().equals(credentials.password())) {
            return userService.getUserById(passwordUser.userId());
        }

        throw new InvalidCredentialsException();
    }
}
