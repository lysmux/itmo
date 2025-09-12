package dev.lysmux.lab8.common.handler.cli;

import dev.lysmux.lab8.common.command.wrapper.CommandWrapper;
import org.jline.reader.LineReader;
import org.jline.reader.impl.DefaultHighlighter;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.util.HashMap;
import java.util.Map;

/**
 * Commands name highlighter
 *
 * @since 1.0
 */
public class CommandsHighlighter extends DefaultHighlighter {
    private final Map<String, CommandWrapper> commands = new HashMap<>();

    public CommandsHighlighter(Map<String, CommandWrapper> commands) {
        this.commands.putAll(commands);
    }

    @Override
    public AttributedString highlight(LineReader reader, String buffer) {
        String[] parts = buffer.split(" ", -1);

        AttributedStringBuilder sb = new AttributedStringBuilder();
        boolean isFirstWord = true;

        for (int i = 0; i < parts.length; i++) {
            String word = parts[i];
            if (isFirstWord) {
                // command
                if (commands.containsKey(word)) {
                    sb.append(AttributedString.stripAnsi(word));
                } else {
                    sb.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED))
                            .append(word)
                            .style(AttributedStyle.DEFAULT);
                }
                isFirstWord = false;
            } else {
                sb.append(AttributedString.stripAnsi(word));
//            } else if (!word.isEmpty()) {
//                // args
//                String commandName = parts[0];
//                if (commands.containsKey(commandName)) {
//                    CommandWrapper cmd = commands.get(commandName);
//                    int argIndex = i - 1;
//                    if (argIndex < cmd.getArgs().size()) {
//                        CommandArg arg = cmd.getArgs().get(argIndex);
//                        if (!isValidArgument(word, arg.type())) {
//                            sb.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED))
//                                    .append(word)
//                                    .style(AttributedStyle.DEFAULT);
//                        } else {
//                            sb.append(AttributedString.stripAnsi(word));
//                        }
//                    } else {
//                        sb.append(AttributedString.stripAnsi(word));
//                    }
//                } else {
//                    sb.append(AttributedString.stripAnsi(word));
//                }
            }
            sb.append(" ");
        }

        return sb.toAttributedString();
    }

//    private boolean isValidArgument(String input, Class<?> type) {
//        try {
//            if (type == Integer.class) {
//                Integer.parseInt(input);
//            } else if (type == Double.class) {
//                Double.parseDouble(input);
//            } else if (type == Boolean.class) {
//                if (!input.equalsIgnoreCase("true") && !input.equalsIgnoreCase("false")) {
//                    throw new IllegalArgumentException();
//                }
//            }
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
}
