package dev.lysmux.lab4.auth.providers.passkey.repository;

public interface PassKeyChallengeRepository {
    void addChallenge(String operationId, byte[] challenge);

    byte[] getChallenge(String operationId);
}
