package dev.lysmux.lab4.auth.providers.passkey;

import com.webauthn4j.WebAuthnManager;
import com.webauthn4j.converter.util.JsonConverter;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.credential.CredentialRecord;
import com.webauthn4j.credential.CredentialRecordImpl;
import com.webauthn4j.data.*;
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier;
import com.webauthn4j.data.client.Origin;
import com.webauthn4j.data.client.challenge.Challenge;
import com.webauthn4j.server.ServerProperty;
import com.webauthn4j.util.Base64UrlUtil;
import com.webauthn4j.verifier.exception.VerificationException;
import dev.lysmux.lab4.auth.providers.passkey.exception.InvalidKeyException;
import dev.lysmux.lab4.auth.providers.passkey.model.PassKeyCredential;
import dev.lysmux.lab4.auth.providers.passkey.model.PassKeyLoginFinishRequest;
import dev.lysmux.lab4.auth.providers.passkey.model.PassKeyLoginStart;
import dev.lysmux.lab4.auth.providers.passkey.repository.PassKeyChallengeRepository;
import dev.lysmux.lab4.auth.providers.passkey.repository.PassKeyCredentialsRepository;
import dev.lysmux.lab4.domain.model.User;
import dev.lysmux.lab4.service.user.UserService;
import dev.lysmux.lab4.utils.UUIDUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PassKeyAuthProvider {
    private static final String RP_ID = "tunnel.lysmux.dev";
    private static final String RP_NAME = "ITMO Web 4";
    private static final String ORIGIN = "https://tunnel.lysmux.dev";

    private static final JsonConverter jsonConverter = new ObjectConverter().getJsonConverter();
    private static final WebAuthnManager webAuthnManager = WebAuthnManager.createNonStrictWebAuthnManager();

    @Inject
    private UserService userService;

    @Inject
    private PassKeyChallengeRepository challengeRepository;

    @Inject
    private PassKeyCredentialsRepository credentialsRepository;

    public String registerStart(String userId) {
        User user = userService.getUserById(userId);

        Challenge challenge = new ChallengeImpl();
        challengeRepository.addChallenge(user.id(), challenge.getValue());

        byte[] userHandle = UUIDUtils.convertUUIDToBytes(UUID.fromString(user.id()));

        PublicKeyCredentialUserEntity userIdentity = new PublicKeyCredentialUserEntity(
                userHandle,
                user.username(),
                user.username()
        );

        PublicKeyCredentialRpEntity rp = new PublicKeyCredentialRpEntity(RP_ID, RP_NAME);
        List<PublicKeyCredentialParameters> pubKeyCredParams = List.of(
                new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES256),
                new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.RS256)
        );

        AuthenticatorSelectionCriteria authenticatorSelection = new AuthenticatorSelectionCriteria(
                AuthenticatorAttachment.PLATFORM,
                true,
                UserVerificationRequirement.PREFERRED
        );

        List<PublicKeyCredentialDescriptor> excludeCredentials = credentialsRepository.getCredentialByUser(user.id())
                .stream()
                .map(cr -> new PublicKeyCredentialDescriptor(
                        PublicKeyCredentialType.PUBLIC_KEY,
                        cr.credentialRecord().getAttestedCredentialData().getCredentialId(),
                        null
                ))
                .toList();

        PublicKeyCredentialCreationOptions options = new PublicKeyCredentialCreationOptions(
                rp,
                userIdentity,
                challenge,
                pubKeyCredParams,
                60_000L,
                excludeCredentials,
                authenticatorSelection,
                AttestationConveyancePreference.NONE,
                null
        );

        return jsonConverter.writeValueAsString(options);
    }

    public void registerFinish(String userId, String request) {
        RegistrationData registrationData = webAuthnManager.parseRegistrationResponseJSON(request);

        ServerProperty serverProperty = new ServerProperty(
                new Origin(ORIGIN),
                RP_ID,
                () -> challengeRepository.getChallenge(userId)
        );

        List<PublicKeyCredentialParameters> pubKeyCredParams = null;
        boolean userVerificationRequired = false;
        boolean userPresenceRequired = true;

        RegistrationParameters registrationParameters = new RegistrationParameters(
                serverProperty,
                pubKeyCredParams,
                userVerificationRequired,
                userPresenceRequired
        );

        try {
            webAuthnManager.verify(registrationData, registrationParameters);
        } catch (VerificationException e) {
            throw e;
        }

        CredentialRecord credentialRecord =
                new CredentialRecordImpl(
                        registrationData.getAttestationObject(),
                        registrationData.getCollectedClientData(),
                        registrationData.getClientExtensions(),
                        registrationData.getTransports()
                );

        credentialsRepository.addCredential(new PassKeyCredential(
                userId,
                credentialRecord
        ));
    }

    public PassKeyLoginStart loginStart() {
        Challenge challenge = new ChallengeImpl();
        String operationId = UUID.randomUUID().toString();

        challengeRepository.addChallenge(operationId, challenge.getValue());

        PublicKeyCredentialRequestOptions options = new PublicKeyCredentialRequestOptions(
                challenge,
                60_000L,
                RP_ID,
                null,
                UserVerificationRequirement.PREFERRED,
                null
        );

        return new PassKeyLoginStart(
                jsonConverter.writeValueAsString(options),
                operationId
        );
    }

    public String loginFinish(PassKeyLoginFinishRequest request) {
        AuthenticationData authenticationData = webAuthnManager.parseAuthenticationResponseJSON(request.loginResponseJSON().toString());

        ServerProperty serverProperty = new ServerProperty(
                new Origin(ORIGIN),
                RP_ID,
                () -> challengeRepository.getChallenge(request.operationId())
        );

// expectations
        List<byte[]> allowCredentials = null;
        boolean userVerificationRequired = true;
        boolean userPresenceRequired = true;

        PassKeyCredential passKeyCredential = credentialsRepository.getCredential(Base64UrlUtil.encodeToString(authenticationData.getCredentialId()));
        if (passKeyCredential == null) {
            throw new InvalidKeyException();
        }

        AuthenticationParameters authenticationParameters =
                new AuthenticationParameters(
                        serverProperty,
                        passKeyCredential.credentialRecord(),
                        allowCredentials,
                        userVerificationRequired,
                        userPresenceRequired
                );

        try {
            webAuthnManager.verify(authenticationData, authenticationParameters);
        } catch (VerificationException e) {
            throw new InvalidKeyException();
        }

        return passKeyCredential.userId();
    }
}
