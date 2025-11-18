package dev.lysmux.lab4.auth.providers.vk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExchangeResponse(long user_id) {
}
