package dev.lysmux.lab4.auth.providers.passkey.model;

import com.fasterxml.uuid.Generators;
import com.webauthn4j.credential.CredentialRecord;

public record PassKeyCredential(
        String id,
        String userId,
        CredentialRecord credentialRecord
) {
    public PassKeyCredential(String userId, CredentialRecord credentialRecord) {
        this(
                Generators.timeBasedEpochRandomGenerator().generate().toString(),
                userId,
                credentialRecord
        );
    }
}
