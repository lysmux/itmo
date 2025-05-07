package dev.lysmux.lab8.common.dto;

import java.io.Serializable;

public record Auth(String login, String password) implements Serializable {
}
