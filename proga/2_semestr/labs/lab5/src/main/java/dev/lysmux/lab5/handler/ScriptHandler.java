package dev.lysmux.lab5.handler;

import dev.lysmux.lab5.collection.validator.ValidationException;
import dev.lysmux.lab5.controller.CommandManager;
import dev.lysmux.lab5.controller.exception.CommandNotFoundException;
import dev.lysmux.lab5.handler.exception.NoSuchArgException;
import dev.lysmux.lab5.handler.parser.CouldNotParseArgException;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Scanner;

/**
 * Class that handles scripts content
 *
 * @since 1.0
 */
public class ScriptHandler extends Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptHandler.class);

    private final Scanner scanner;

    public ScriptHandler(@NonNull CommandManager commandManager, String script) {
        super(commandManager);
        this.scanner = new Scanner(script.trim());
    }

    /**
     * Executes given script
     *
     * @return execution result
     */
    public String executeScript() {
        StringBuilder output = new StringBuilder();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            try {
                String result = handleInput(line);
                output.append(result).append(System.lineSeparator());
            } catch (CommandNotFoundException | CouldNotParseArgException | ValidationException exc) {
                output.append(exc.getMessage()).append(System.lineSeparator());
            } catch (NoSuchArgException exc) {
                output.append(exc.getUsage()).append(System.lineSeparator());
            } catch (Exception exc) {
                LOGGER.error("Failed process line {}", line, exc);
            }
        }

        return output.toString();
    }

    /**
     * Gets next script line as object property
     *
     * @param objClass {@inheritDoc}
     * @param field    {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    protected String requestProperty(Class<?> objClass, Field field) {
        return scanner.nextLine();
    }
}
