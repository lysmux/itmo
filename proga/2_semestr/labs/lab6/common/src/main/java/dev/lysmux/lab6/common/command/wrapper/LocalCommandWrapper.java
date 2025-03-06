package dev.lysmux.lab6.common.command.wrapper;

import dev.lysmux.lab6.common.command.Command;
import dev.lysmux.lab6.common.command.exception.CouldNotExecuteCommand;
import dev.lysmux.lab6.common.command.exception.InvalidCommandException;
import dev.lysmux.lab6.common.command.meta.CommandArg;
import dev.lysmux.lab6.common.dto.Response;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class that provides access to the command
 * through Java reflection API
 *
 * @since 1.0
 */
public class LocalCommandWrapper implements CommandWrapper {
    private final Object command;
    private final Command annotation;

    public LocalCommandWrapper(Object command) {
        Class<?> clazz = command.getClass();
        Method[] executeMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.getName().equals("execute")).toArray(Method[]::new);

        if (!clazz.isAnnotationPresent(Command.class)) {
            throw new InvalidCommandException("Command must be annotated with @Command");
        }

        if (executeMethods.length != 1) {
            throw new InvalidCommandException("Command must have exactly one `execute` method");
        }

        if (executeMethods[0].getReturnType() != Response.class) {
            throw new InvalidCommandException("Command must return a `Response` object");
        }

        this.command = command;
        this.annotation = clazz.getAnnotation(Command.class);
    }

    /**
     * Executes command with specified arguments
     *
     * @param args array of command arguments
     * @return execution result
     * @throws CouldNotExecuteCommand if command could not be executed with this arguments
     */
    @Override
    public Response execute(Object... args) {
        Method executeMethod = getExecuteMethod();
        try {
            return (Response) executeMethod.invoke(command, args);
        } catch (ReflectiveOperationException e) {
            throw new CouldNotExecuteCommand(e);
        }
    }

    /**
     * Gets command name from annotation {@link Command}
     *
     * @return command name
     * @see Command
     */
    @Override
    public String getName() {
        return annotation.name();
    }

    /**
     * Gets command description from annotation {@link Command}
     *
     * @return command description
     * @see Command
     */
    @Override
    public String getDescription() {
        return annotation.description();
    }

    /**
     * Checks if command needed to include in help from annotation {@link Command}
     *
     * @return {@code true} if needed else {@code false}
     * @see Command
     */
    public boolean includeInHelp() {
        return annotation.includeInHelp();
    }

    /**
     * Checks if command can be executed only local from annotation {@link Command}
     *
     * @return {@code true} if can else {@code false}
     * @see Command
     */
    @Override
    public boolean local() {
        return annotation.local();
    }

    /**
     * Gets command arguments from execute method
     *
     * @return list of argument name and its type
     */
    @Override
    public List<CommandArg> getArgs() {
        List<CommandArg> commandArgs = new ArrayList<>();
        Method executeMethod = getExecuteMethod();

        for (var param : executeMethod.getParameters()) {
            commandArgs.add(new CommandArg(param.getName(), param.getType()));
        }
        return commandArgs;
    }

    /**
     * Gets command {@code execute} method via reflection
     *
     * @return execute method
     */
    private Method getExecuteMethod() {
        Class<?> clazz = command.getClass();
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.getName().equals("execute"))
                .findFirst()
                .orElse(null);
    }
}
