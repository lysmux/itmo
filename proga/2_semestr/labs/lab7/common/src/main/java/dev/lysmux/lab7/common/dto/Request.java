package dev.lysmux.lab7.common.dto;

import java.io.Serializable;

/**
 * Class that stores request information
 *
 * @param command request command
 * @param args    request args
 * @since 1.0
 */
public record Request(Auth auth, String command, Object[] args) implements Serializable {
    public Request(String command) {
        this(null, command, new Object[]{});
    }

    public Request(String command, Object[] args) {
        this(null, command, args);
    }
}
