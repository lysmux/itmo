package dev.lysmux.lab7.common.handler;

import dev.lysmux.lab7.common.collection.validator.ValidationException;
import dev.lysmux.lab7.common.command.CommandRegistry;
import dev.lysmux.lab7.common.dto.Response;
import dev.lysmux.lab7.common.handler.cli.CommandsCompleter;
import dev.lysmux.lab7.common.handler.cli.CommandsHighlighter;
import dev.lysmux.lab7.common.handler.exception.NoSuchArgException;
import dev.lysmux.lab7.common.handler.parser.ArgParser;
import dev.lysmux.lab7.common.handler.parser.CouldNotParseArgException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;

public class ConsoleHandler extends Handler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ConsoleHandler.class);

    private final LineReader reader;


    public ConsoleHandler(CommandRegistry commandRegistry) {
        super(commandRegistry);

        Terminal terminal = null;
        try {
            terminal = TerminalBuilder.builder().system(true).build();
        } catch (IOException e) {
            log.atError()
                    .setMessage("Could not initialize terminal")
                    .setCause(e)
                    .log();
            System.exit(1);
        }

        reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new CommandsCompleter(commandRegistry.getCommands()))
                .highlighter(new CommandsHighlighter(commandRegistry.getCommands()))
                .build();
        reader.setAutosuggestion(LineReader.SuggestionType.COMPLETER);
    }

    /**
     * Main loop. Wait input from user and handle it
     */
    public void run() {
        printWelcome();

        String input;
        try {
            while ((input = reader.readLine(">> ")) != null) {
                try {
                    Response response = handleInput(input);
                    if (response.success()) printSuccess(response.toString());
                    else printError(response.toString());
                } catch (NoSuchArgException e) {
                    printError(e.getUsage());
                } catch (Exception e) {
                    printError(e.getMessage());
                }
            }
        } catch (UserInterruptException e) {
            log.atInfo()
                    .setMessage("Interrupted by user")
                    .log();
            System.exit(0);
        }
    }

    /**
     * Prints welcome message
     */
    protected void printWelcome() {
        AttributedStringBuilder builder = new AttributedStringBuilder();
        builder.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW).bold())
                .append("""
                                   ____      _ _         \s
                                  / ___|___ | | | _____  __
                                 | |   / _ \\| | |/ _ \\ \\/ /
                                 | |__| (_) | | |  __/>  <\s
                                  \\____\\___/|_|_|\\___/_/\\_\\
                        \s"""
                ).append(System.lineSeparator())
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN))
                .append("Type `help` to show available commands");

        reader.printAbove(builder.toAttributedString());
    }

    public void printError(String message) {
        reader.printAbove(new AttributedString(message, AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)));
    }

    public void printSuccess(String message) {
        reader.printAbove(message);
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

        return reader.readLine(promptBuilder.toString());
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
                printError("%s. Try again".formatted(e.getMessage()));
            }
        }
    }
}
