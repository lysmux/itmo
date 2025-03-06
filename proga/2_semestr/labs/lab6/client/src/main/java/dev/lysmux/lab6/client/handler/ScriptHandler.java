package dev.lysmux.lab6.client.handler;


import dev.lysmux.lab6.common.collection.validator.ValidationException;
import dev.lysmux.lab6.common.command.CommandRegistry;
import dev.lysmux.lab6.common.command.exception.CommandNotFoundException;
import dev.lysmux.lab6.common.handler.Handler;
import dev.lysmux.lab6.common.handler.exception.NoSuchArgException;
import dev.lysmux.lab6.common.handler.parser.CouldNotParseArgException;
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
    private static final Logger log = LoggerFactory.getLogger(ScriptHandler.class);

    private final Scanner scanner;

    public ScriptHandler(CommandRegistry commandRegistry, String script) {
        super(commandRegistry);
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
                String result = handleInput(line).toString();
                output.append(result).append(System.lineSeparator());
            } catch (CommandNotFoundException | CouldNotParseArgException | ValidationException exc) {
                output.append(exc.getMessage()).append(System.lineSeparator());
            } catch (NoSuchArgException exc) {
                output.append(exc.getUsage()).append(System.lineSeparator());
            } catch (Exception exc) {
                log.error("Failed process line {}", line, exc);
            }
        }

        return output.toString();
    }

    @Override
    protected String requestProperty(Class<?> objClass, Field field) {
        return scanner.nextLine();
    }
}
