package dev.lysmux.lab4.api.resource;

import com.webauthn4j.WebAuthnManager;
import com.webauthn4j.converter.util.JsonConverter;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.credential.CredentialRecord;
import com.webauthn4j.credential.CredentialRecordImpl;
import com.webauthn4j.data.*;
import com.webauthn4j.data.attestation.AttestationObject;
import com.webauthn4j.data.attestation.authenticator.AuthenticatorData;
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier;
import com.webauthn4j.data.attestation.statement.NoneAttestationStatement;
import com.webauthn4j.data.client.Origin;
import com.webauthn4j.data.client.challenge.Challenge;
import com.webauthn4j.server.ServerProperty;
import com.webauthn4j.util.Base64UrlUtil;
import com.webauthn4j.verifier.exception.VerificationException;
import dev.lysmux.lab4.UUIDUtils;
import dev.lysmux.lab4.api.filter.Secured;
import dev.lysmux.lab4.auth.providers.passkey.*;
import dev.lysmux.lab4.auth.providers.password.PasswordAuthProvider;
import dev.lysmux.lab4.auth.providers.password.PasswordCredentials;
import dev.lysmux.lab4.auth.providers.vk.VKAuthProvider;
import dev.lysmux.lab4.auth.providers.vk.VKCredentials;
import dev.lysmux.lab4.domain.TokensPair;
import dev.lysmux.lab4.domain.User;
import dev.lysmux.lab4.repository.PassKeyChallengeRepository;
import dev.lysmux.lab4.repository.PassKeyCredentialsRepository;
import dev.lysmux.lab4.schemas.auth.AuthResponse;
import dev.lysmux.lab4.schemas.auth.LoginRequest;
import dev.lysmux.lab4.schemas.auth.RegisterRequest;
import dev.lysmux.lab4.schemas.auth.VKCallbackRequest;
import dev.lysmux.lab4.service.AuthService;
import dev.lysmux.lab4.service.UserService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class AuthResource {
    @Inject
    private UserService userService;

    @Inject
    private AuthService authService;

    @Inject
    private VKAuthProvider vkAuthProvider;

    @Inject
    private PasswordAuthProvider passwordAuthProvider;

    @POST
    @Path("/register")
    public Response register(@Valid RegisterRequest request) {
        User user = passwordAuthProvider.register(new PasswordCredentials(
                request.username(),
                request.password()
        ));
        TokensPair tokensPair = authService.generateTokensPair(user.id());
        return makeTokenResponse(tokensPair);
    }

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequest request) {
        User user = passwordAuthProvider.login(new PasswordCredentials(
                request.username(),
                request.password()
        ));
        TokensPair tokensPair = authService.generateTokensPair(user.id());
        return makeTokenResponse(tokensPair);
    }

    @POST
    @Path("/logout")
    public Response logout(@NotNull @CookieParam("refreshToken") String refreshToken) {
        authService.banToken(refreshToken);
        return makeClearTokenResponse();
    }

    @POST
    @Path("/refresh")
    public Response refreshTokens(@NotNull @CookieParam("refreshToken") String refreshToken) {
        TokensPair tokensPair = authService.refreshTokensPair(refreshToken);
        return makeTokenResponse(tokensPair);
    }

    @POST
    @Path("/callback/vk")
    public Response vkCallback(@Valid VKCallbackRequest request) {
        User user = vkAuthProvider.register(new VKCredentials(
                request.code(),
                request.deviceId(),
                request.challengeVerifier()
        ));
        TokensPair tokensPair = authService.generateTokensPair(user.id());
        return makeTokenResponse(tokensPair);
    }

    @Inject
    private PassKeyChallengeRepository challengeRepository;

    @Inject
    private PassKeyCredentialsRepository credentialsRepository;

    @GET
    @Path("/passkey/register")
    @Secured
    public Response passkeyRegisterStart(@Context SecurityContext securityContext) {
        User user = userService.getUser(securityContext.getUserPrincipal().getName());

        Challenge challenge = new ChallengeImpl();
        challengeRepository.addChallenge(user.id(), challenge.getValue());

        byte[] userHandle = UUIDUtils.convertUUIDToBytes(UUID.fromString(user.id()));

        PublicKeyCredentialUserEntity userIdentity = new PublicKeyCredentialUserEntity(
                userHandle,
                user.username(),
                user.username()
        );

        PublicKeyCredentialRpEntity rp = new PublicKeyCredentialRpEntity("tunnel.lysmux.dev", "ITMO Web 4");

        List<PublicKeyCredentialParameters> pubKeyCredParams = List.of(
                new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES256),
                new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.RS256)
        );

        AuthenticatorSelectionCriteria authenticatorSelection = new AuthenticatorSelectionCriteria(
                AuthenticatorAttachment.PLATFORM,
                true,
                UserVerificationRequirement.PREFERRED
        );

        // Исключаем уже зарегистрированные credentialId
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

        var objectConverter = new ObjectConverter();
        JsonConverter jsonConverter = objectConverter.getJsonConverter();
        return Response.ok(jsonConverter.writeValueAsString(options)).build();
    }


    @POST
    @Path("/passkey/register")
    @Secured
    public Response passkeyRegisterFinish(@Context SecurityContext securityContext, String request) throws Exception {

        WebAuthnManager webAuthnManager = WebAuthnManager.createNonStrictWebAuthnManager();
        RegistrationData registrationData = webAuthnManager.parseRegistrationResponseJSON(request);

        Origin origin = new Origin("https://tunnel.lysmux.dev");
        String rpId = "tunnel.lysmux.dev";
        ServerProperty serverProperty = new ServerProperty(
                origin,
                rpId,
                () -> challengeRepository.getChallenge(securityContext.getUserPrincipal().getName())
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
            // If you would like to handle WebAuthn data verification error, please catch VerificationException
            throw e;
        }

        CredentialRecord credentialRecord =
                new CredentialRecordImpl( // You may create your own CredentialRecord implementation to save friendly authenticator name
                        registrationData.getAttestationObject(),
                        registrationData.getCollectedClientData(),
                        registrationData.getClientExtensions(),
                        registrationData.getTransports()
                );

        credentialsRepository.addCredential(new PassKeyCredential(
                securityContext.getUserPrincipal().getName(),
                credentialRecord
        ));

        return Response.ok().build();
    }

    @GET
    @Path("/passkey/login")
    public PassKeyLoginStart passkeyLoginStart() {
        Challenge challenge = new ChallengeImpl();
        String operationId = UUID.randomUUID().toString();

        challengeRepository.addChallenge(operationId, challenge.getValue());

        PublicKeyCredentialRequestOptions options = new PublicKeyCredentialRequestOptions(
                challenge,
                60_000L,
                "tunnel.lysmux.dev",
                null,
                UserVerificationRequirement.PREFERRED,
                null
        );
        var objectConverter = new ObjectConverter();
        JsonConverter jsonConverter = objectConverter.getJsonConverter();
        return new PassKeyLoginStart(
                jsonConverter.writeValueAsString(options),
                operationId
        );
    }


    @POST
    @Path("/passkey/login")
    public Response passkeyLoginFinish(PassKeyLoginFinishRequest request) {
        WebAuthnManager webAuthnManager = WebAuthnManager.createNonStrictWebAuthnManager();
        AuthenticationData authenticationData = webAuthnManager.parseAuthenticationResponseJSON(request.loginResponseJSON().toString());

        Origin origin = new Origin("https://tunnel.lysmux.dev");
        String rpId = "tunnel.lysmux.dev";
        ServerProperty serverProperty = new ServerProperty(
                origin,
                rpId,
                () -> challengeRepository.getChallenge(request.operationId())
        );

// expectations
        List<byte[]> allowCredentials = null;
        boolean userVerificationRequired = true;
        boolean userPresenceRequired = true;

        PassKeyCredential passKeyCredential = credentialsRepository.getCredential(Base64UrlUtil.encodeToString(authenticationData.getCredentialId()));
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
            // If you would like to handle WebAuthn data validation error, please catch ValidationException
            throw e;
        }

        TokensPair tokensPair = authService.generateTokensPair(passKeyCredential.userId());
        return makeTokenResponse(tokensPair);
    }

    private Response makeTokenResponse(TokensPair tokensPair) {
        AuthResponse response = new AuthResponse(tokensPair.accessToken().expiresIn());

        NewCookie refreshTokenCookie = new NewCookie.Builder("refreshToken")
                .value(tokensPair.refreshToken().token())
                .maxAge(tokensPair.refreshToken().expiresIn())
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite(NewCookie.SameSite.LAX)
                .build();

        NewCookie accessTokenCookie = new NewCookie.Builder("accessToken")
                .value(tokensPair.accessToken().token())
                .maxAge(tokensPair.accessToken().expiresIn())
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite(NewCookie.SameSite.LAX)
                .build();

        return Response
                .ok(response)
                .cookie(accessTokenCookie, refreshTokenCookie)
                .build();
    }

    private Response makeClearTokenResponse() {
        NewCookie refreshTokenCookie = new NewCookie.Builder("refreshToken")
                .maxAge(0)
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite(NewCookie.SameSite.LAX)
                .build();

        NewCookie accessTokenCookie = new NewCookie.Builder("accessToken")
                .maxAge(0)
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite(NewCookie.SameSite.LAX)
                .build();

        return Response
                .ok()
                .cookie(accessTokenCookie, refreshTokenCookie)
                .build();
    }
}
