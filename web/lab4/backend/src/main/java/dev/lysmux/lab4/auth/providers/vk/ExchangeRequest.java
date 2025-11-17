package dev.lysmux.lab4.auth.providers.vk;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExchangeRequest {
    @Builder.Default
    private String grant_type = "authorization_code";
    private String code;
    private String code_verifier;
    private String client_id;
    private String device_id;
    private String redirect_uri;
    private String state;
}