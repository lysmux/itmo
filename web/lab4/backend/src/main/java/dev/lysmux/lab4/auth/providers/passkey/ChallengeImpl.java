package dev.lysmux.lab4.auth.providers.passkey;

import com.webauthn4j.data.client.challenge.Challenge;

import java.security.SecureRandom;
import java.util.Arrays;

public class ChallengeImpl implements Challenge {
    private final byte[] value;

    public ChallengeImpl() {
        this.value = new byte[32];  // 32 байта = 256 бит
        new SecureRandom().nextBytes(this.value);
    }

    @Override
    public byte[] getValue() {
        return Arrays.copyOf(value, value.length);
    }
}