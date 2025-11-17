package dev.lysmux.lab4.auth.providers.vk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExchangeResponse(int user_id) {
}
