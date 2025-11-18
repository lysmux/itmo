package dev.lysmux.lab4.repository.memory;

import dev.lysmux.lab4.auth.providers.passkey.repository.PassKeyChallengeRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class MemoryPassKeyChallengeRepository implements PassKeyChallengeRepository {
    private final Map<String, byte[]> challenges = new HashMap<>();

    public void addChallenge(String operationId, byte[] challenge) {
        challenges.put(operationId, challenge);
    }

    public byte[] getChallenge(String operationId) {
        return challenges.get(operationId);
    }
}
