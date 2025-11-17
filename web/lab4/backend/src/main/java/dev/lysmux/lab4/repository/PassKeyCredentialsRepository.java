package dev.lysmux.lab4.repository;

import com.webauthn4j.util.Base64UrlUtil;
import dev.lysmux.lab4.auth.providers.passkey.PassKeyCredential;
import dev.lysmux.lab4.auth.providers.password.PasswordUser;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PassKeyCredentialsRepository {
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
