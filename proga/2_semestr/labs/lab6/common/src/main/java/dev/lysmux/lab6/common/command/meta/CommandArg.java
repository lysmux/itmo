package dev.lysmux.lab6.common.command.meta;

import java.io.Serializable;

/**
 * Record that stores metadata of command argument
 *
 * @param name arg name
 * @param type arg type
 */
public record CommandArg(String name, Class<?> type) implements Serializable {
}
