package dev.lysmux.lab5.handler;

import dev.lysmux.lab5.collection.validator.ValidationException;
import dev.lysmux.lab5.controller.CommandManager;
import dev.lysmux.lab5.controller.exception.CommandNotFoundException;
import dev.lysmux.lab5.handler.exception.NoSuchArgException;
import dev.lysmux.lab5.handler.parser.ArgParser;
import dev.lysmux.lab5.handler.parser.CouldNotParseArgException;
import dev.lysmux.lab5.io.console.Console;
import dev.lysmux.lab5.io.console.StandartConsole;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Class that handles user input from console
 *
 * @since 1.0
 */
public class ConsoleHandler extends Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleHandler.class);

    private final Console console = new StandartConsole();

    public ConsoleHandler(@NonNull CommandManager commandManager) {
        super(commandManager);
    }

    /**
     * Main loop. Wait input from user and handle it
     */
    public void run() {
        emitStart();

        String input;
        while ((input = console.read("Enter command: ")) != null) {
            try {
                console.writeln(handleInput(input));
            } catch (CommandNotFoundException | CouldNotParseArgException exc) {
                console.writeln(exc.getMessage());
            } catch (NoSuchArgException exc) {
                console.writeln(exc.getUsage());
            } catch (Exception exc) {
                LOGGER.error("Failed process input", exc);
            }
        }
    }

    /**
     * Called at handle startup
     */
    protected void emitStart() {
        try {
            console.writeln(handleInput("check_backup"));
        } catch (Exception e) {
            LOGGER.error("Failed emit start", e);
        }
    }

    /**
     * Constructs prompt to request object property
     *
     * @param objClass object class that is being parsed
     * @param field    field that is being parsed
     * @return constructed prompt
     */
    private static String constructPrompt(Class<?> objClass, Field field) {
        Class<?> fieldType = field.getType();
        StringBuilder promptBuilder = new StringBuilder();

        switch (ArgParser.getArgType(fieldType)) {
            case ENUM:
                promptBuilder.append("Possible values %s. ".formatted(Arrays.toString(fieldType.getEnumConstants())));
                break;
            case DATE:
                promptBuilder.append("Format DD.MM.YYYY. ");
                break;
        }
        promptBuilder.append("Enter %s.%s: ".formatted(objClass.getSimpleName(), field.getName()));

        return promptBuilder.toString();
    }

    /**
     * Gets next user input as object property
     *
     * @param objClass {@inheritDoc}
     * @param field    {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    protected String requestProperty(Class<?> objClass, Field field) {
        return console.read(constructPrompt(objClass, field));
    }

    /**
     * Request object property until it is valid
     *
     * @param objClass {@inheritDoc}
     * @param field    {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    protected Object parseObjectField(Class<?> objClass, Field field) {
        while (true) {
            try {
                return super.parseObjectField(objClass, field);
            } catch (CouldNotParseArgException | ValidationException e) {
                console.writeln("Invalid input: %s. Try again".formatted(e.getMessage()));
            }
        }
    }
}
