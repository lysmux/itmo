package dev.lysmux.lab4.auth.providers.vk.repository;


import dev.lysmux.lab4.auth.providers.vk.model.VKUser;

public interface VKAuthRepository {
    VKUser addUser(VKUser user);

    VKUser getUser(long vkId);
}
