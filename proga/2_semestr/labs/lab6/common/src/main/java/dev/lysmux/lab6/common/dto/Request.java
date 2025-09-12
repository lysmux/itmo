package dev.lysmux.lab6.common.dto;

import java.io.Serializable;

/**
 * Class that stores request information
 *
 * @param command request command
 * @param args    request args
 * @since 1.0
 */
public record Request(String command, Object[] args) implements Serializable {
    public Request(String command) {
        this(command, new Object[]{});
    }
}
