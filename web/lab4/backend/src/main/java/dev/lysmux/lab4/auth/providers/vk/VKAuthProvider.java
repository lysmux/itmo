package dev.lysmux.lab4.auth.providers.vk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.uuid.Generators;
import dev.lysmux.lab4.auth.providers.AuthProvider;
import dev.lysmux.lab4.domain.User;
import dev.lysmux.lab4.repository.VKAuthRepository;
import dev.lysmux.lab4.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ApplicationScoped
public class VKAuthProvider implements AuthProvider<VKCredentials> {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    @Inject
    private VKAuthRepository repository;

    @Inject
    private UserService userService;

    @Override
    public User register(VKCredentials credentials) {
        ExchangeResponse exchanged = exchangeCode(credentials);
        VKUser vkUser = repository.getUser(exchanged.user_id());

        if (vkUser == null) {
            User user = userService.createUser("vk_" + exchanged.user_id());
            vkUser = new VKUser(exchanged.user_id(), user.id());
            repository.addUser(vkUser);
            return user;
        }

        return userService.getUser(vkUser.userId());
    }

    @Override
    public User login(VKCredentials credentials) {
        return register(credentials);
    }

    private ExchangeResponse exchangeCode(VKCredentials credentials) {
        ExchangeRequest request = ExchangeRequest.builder()
                .code(credentials.code())
                .code_verifier(credentials.challengeVerifier())
                .client_id("54324524")
                .device_id(credentials.deviceId())
                .redirect_uri("https://tunnel.lysmux.dev/auth/callback/vk")
                .state(Generators.randomBasedGenerator().generate().toString())
                .build();

        return makeRequest(request);
    }

    private ExchangeResponse makeRequest(ExchangeRequest request) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(java.net.URI.create("https://id.vk.ru/oauth2/auth"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(request)))
                    .build();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), ExchangeResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
