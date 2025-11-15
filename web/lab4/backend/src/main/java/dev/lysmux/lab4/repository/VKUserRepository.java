package dev.lysmux.lab4.repository;

import dev.lysmux.lab4.domain.User;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class VKUserRepository {
    private final Map<Integer, User> users = new HashMap<>();

    public User getUser(int vkId) {
        return users.get(vkId);
    }
}
