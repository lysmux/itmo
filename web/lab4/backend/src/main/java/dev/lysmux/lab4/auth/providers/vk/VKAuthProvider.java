package dev.lysmux.lab4.auth.providers.vk;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lysmux.lab4.auth.providers.vk.exception.VKAuthException;
import dev.lysmux.lab4.auth.providers.vk.model.ExchangeRequest;
import dev.lysmux.lab4.auth.providers.vk.model.ExchangeResponse;
import dev.lysmux.lab4.auth.providers.vk.model.VKCredentials;
import dev.lysmux.lab4.auth.providers.vk.model.VKUser;
import dev.lysmux.lab4.auth.providers.vk.repository.VKAuthRepository;
import dev.lysmux.lab4.domain.model.User;
import dev.lysmux.lab4.service.user.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@ApplicationScoped
public class VKAuthProvider {
    private static final String EXCHANGE_URL = "https://id.vk.ru/oauth2/auth";

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    @Inject
    private VKAuthRepository repository;

    @Inject
    private UserService userService;

    @Transactional
    public User register(VKCredentials credentials) {
        ExchangeResponse exchanged = exchangeCode(credentials);
        VKUser vkUser = repository.getUser(exchanged.user_id());

        if (vkUser == null) {
            User user = userService.createUser("vk_" + exchanged.user_id());
            vkUser = new VKUser(exchanged.user_id(), user.id());
            repository.addUser(vkUser);
            return user;
        }

        return userService.getUserById(vkUser.userId());
    }

    private ExchangeResponse exchangeCode(VKCredentials credentials) {
        ExchangeRequest request = ExchangeRequest.builder()
                .code(credentials.code())
                .code_verifier(credentials.challengeVerifier())
                .device_id(credentials.deviceId())
                .state(UUID.randomUUID().toString())
                .build();

        return makeRequest(request);
    }

    private ExchangeResponse makeRequest(ExchangeRequest request) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(java.net.URI.create(EXCHANGE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(request)))
                    .build();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), ExchangeResponse.class);
        } catch (IOException | InterruptedException e) {
            throw new VKAuthException("Could not exchange token", e);
        }
    }
}
