package dev.lysmux.lab7.common.dto;

import java.io.Serializable;

public record Auth(String login, String password) implements Serializable {
}
