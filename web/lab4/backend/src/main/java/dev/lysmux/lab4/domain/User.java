package dev.lysmux.lab4.domain;

import com.fasterxml.uuid.Generators;

public record User(
        String id,
        String username
) {
    public User(String username) {
        this(Generators.timeBasedEpochRandomGenerator().generate().toString(), username);
    }
}
