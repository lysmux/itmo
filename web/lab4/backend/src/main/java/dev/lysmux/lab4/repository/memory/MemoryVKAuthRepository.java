package dev.lysmux.lab4.repository.memory;

import dev.lysmux.lab4.auth.providers.vk.model.VKUser;
import dev.lysmux.lab4.auth.providers.vk.repository.VKAuthRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Alternative
public class MemoryVKAuthRepository implements VKAuthRepository {
    private final List<VKUser> users = new ArrayList<>();

    @Override
    public VKUser addUser(VKUser user) {
        users.add(user);
        return user;
    }

    @Override
    public VKUser getUser(long vkId) {
        return users.stream()
                .filter(user -> user.vkId() == vkId)
                .findFirst()
                .orElse(null);
    }
}
