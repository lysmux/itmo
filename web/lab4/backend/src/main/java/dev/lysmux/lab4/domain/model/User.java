package dev.lysmux.lab4.domain.model;

import com.fasterxml.uuid.Generators;
import lombok.Builder;

@Builder
public record User(
        String id,
        String username
) {
    public User(String username) {
        this(null, username);
    }
}
