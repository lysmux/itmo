package dev.lysmux.lab4.auth.providers.passkey.repository;

import dev.lysmux.lab4.auth.providers.passkey.model.PassKeyCredential;

import java.util.List;

public interface PassKeyCredentialsRepository {
    void addCredential(PassKeyCredential credential);

    PassKeyCredential getCredential(String id);

    List<PassKeyCredential> getCredentialByUser(String userId);
}
