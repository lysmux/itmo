package dev.lysmux.lab4.auth.providers.vk.model;

import lombok.Builder;

@Builder
public record VKUser(
        long vkId,
        String userId
) {
}
