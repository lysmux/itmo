package dev.lysmux.lab4.repository.memory;

import com.webauthn4j.util.Base64UrlUtil;
import dev.lysmux.lab4.auth.providers.passkey.model.PassKeyCredential;
import dev.lysmux.lab4.auth.providers.passkey.repository.PassKeyCredentialsRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MemoryPassKeyCredentialsRepository implements PassKeyCredentialsRepository {
    private final List<PassKeyCredential> credentials = new ArrayList<>();

    public void addCredential(PassKeyCredential credential) {
        credentials.add(credential);
    }

    public PassKeyCredential getCredential(String id) {
        return credentials.stream()
                .filter(credential -> (Base64UrlUtil.encodeToString(credential.credentialRecord().getAttestedCredentialData().getCredentialId())).equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<PassKeyCredential> getCredentialByUser(String userId) {
        return credentials.stream()
                .filter(credential -> credential.userId().equals(userId))
                .toList();
    }
}
