package dev.lysmux.lab4.auth.providers;

import dev.lysmux.lab4.domain.User;

public interface AuthProvider<T> {
    User register(T credentials);
    User login(T credentials);
}
