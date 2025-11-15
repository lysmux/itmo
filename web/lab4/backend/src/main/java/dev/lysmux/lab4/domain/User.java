package dev.lysmux.lab4.domain;

import com.fasterxml.uuid.Generators;

public record User(
        String id,
        String username,
        String password
) {
    public User(String username, String password) {
        this(Generators.timeBasedEpochRandomGenerator().generate().toString(), username, password);
    }
}
