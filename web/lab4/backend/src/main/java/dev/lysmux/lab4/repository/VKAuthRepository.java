package dev.lysmux.lab4.repository;

import dev.lysmux.lab4.auth.providers.vk.VKUser;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class VKAuthRepository {
    private final List<VKUser> users = new ArrayList<>();

    public void addUser(VKUser user) {
        users.add(user);
    }

    public VKUser getUser(int vkId) {
        return users.stream()
                .filter(user -> user.vkId() == vkId)
                .findFirst()
                .orElse(null);
    }
}
