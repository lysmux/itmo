package dev.lysmux.lab4;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.uuid.Generators;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@ApplicationScoped
@Slf4j
public class VKAuthProvider {
    private HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper(); // Jackson

    @Builder
    @Data
    private static class VKRequest {
        @Builder.Default
        private String grant_type = "authorization_code";
        private String code;
        private String code_verifier;
        private String client_id;
        private String device_id;
        private String redirect_uri;
        private String state;
    }

    record VKResponse(
            int user_id
    ) {}

    public record VKUser(int id, String username) {}

    public VKUser exchangeCode(String code, String deviceId, String challengeVerifier) {
        VKRequest vkRequest = VKRequest.builder()
                .code(code)
                .code_verifier(challengeVerifier)
                .client_id("54324524")
                .device_id(deviceId)
                .redirect_uri("https://tunnel.lysmux.dev/auth/callback/vk")
                .state(Generators.randomBasedGenerator().generate().toString())
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create("https://id.vk.ru/oauth2/auth"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(vkRequest)))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            VKResponse vkResponse = mapper.readValue(response.body(), VKResponse.class);

            return new VKUser(vkResponse.user_id(), "vk_" + vkResponse.user_id());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
