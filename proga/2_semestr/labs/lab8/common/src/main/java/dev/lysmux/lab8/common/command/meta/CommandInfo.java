package dev.lysmux.lab8.common.command.meta;

import java.io.Serializable;
import java.util.List;

/**
 * Class that stores metadata of command
 *
 * @param name        command name
 * @param description command description
 * @param args        command arguments
 */
public record CommandInfo(
        String name,
        String description,
        List<CommandArg> args
) implements Serializable {
}
