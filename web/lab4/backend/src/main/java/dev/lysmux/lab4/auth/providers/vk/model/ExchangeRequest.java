package dev.lysmux.lab4.auth.providers.vk.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExchangeRequest {
    @Builder.Default
    private String grant_type = "authorization_code";
    @Builder.Default
    private String client_id = "54324524";
    @Builder.Default
    private String redirect_uri = "https://tunnel.lysmux.dev/auth/callback/vk";

    private String code;
    private String code_verifier;
    private String device_id;
    private String state;
}