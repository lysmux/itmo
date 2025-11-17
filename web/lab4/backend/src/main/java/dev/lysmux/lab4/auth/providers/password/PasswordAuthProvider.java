package dev.lysmux.lab4.auth.providers.password;

import dev.lysmux.lab4.InvalidCredentialsException;
import dev.lysmux.lab4.auth.providers.AuthProvider;
import dev.lysmux.lab4.domain.User;
import dev.lysmux.lab4.repository.PasswordAuthRepository;
import dev.lysmux.lab4.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PasswordAuthProvider implements AuthProvider<PasswordCredentials> {
    @Inject
    private UserService userService;

    @Inject
    private PasswordAuthRepository repository;

    @Override
    public User register(PasswordCredentials credentials) {
        User user = userService.createUser(credentials.username());
        repository.addUser(new PasswordUser(user.id(), credentials.username(), credentials.password()));

        return user;
    }

    @Override
    public User login(PasswordCredentials credentials) {
        PasswordUser passwordUser = repository.getUser(credentials.username());

        if (passwordUser != null && passwordUser.password().equals(credentials.password())) {
            return userService.getUser(passwordUser.userId());
        }

        throw new InvalidCredentialsException("Invalid username or password");
    }
}
