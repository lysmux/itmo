package dev.lysmux.lab6.common.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for mark class as command
 *
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {
    /**
     * Name of the command by which it will be executed
     *
     * @return command name
     */
    String name();

    /**
     * Description of the command which will be displayed by <b>help</b> command
     *
     * @return command description
     */
    String description();

    /**
     * Needed to include command in help
     *
     * @return {@code true} if needed else {@code false}
     */
    boolean includeInHelp() default true;

    /**
     * Can be displayed only in console local mode
     *
     * @return {@code true} if can else {@code false}
     */
    boolean local() default false;
}
