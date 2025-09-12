package dev.lysmux.lab7.common.handler.cli;

import dev.lysmux.lab7.common.command.meta.CommandArg;
import dev.lysmux.lab7.common.command.wrapper.CommandWrapper;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Commands name and args completer
 *
 * @since 1.0
 */
public class CommandsCompleter implements Completer {
    private final HashMap<String, CommandWrapper> commands = new HashMap<>();

    public CommandsCompleter(Map<String, CommandWrapper> commands) {
        this.commands.putAll(commands);
    }

    /**
     * Compute candidates to complete
     *
     * @param reader     console reader
     * @param line       input line
     * @param candidates complete candidates
     */
    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        List<String> words = line.words();
        int wordCount = words.size();

        if (wordCount <= 1) {
            commands.keySet().forEach(cmd -> candidates.add(new Candidate(cmd)));
        } else {
            String commandName = words.get(0);
            if (commands.containsKey(commandName)) {
                CommandWrapper cmd = commands.get(commandName);

                int argIndex = wordCount - 2;
                if (argIndex < cmd.getArgs().size()) {
                    CommandArg arg = cmd.getArgs().get(argIndex);
                    String description = "%s:%s".formatted(arg.name(), arg.type().getSimpleName());
                    candidates.add(new Candidate("", "", null, description, null, null, false));
                }
            }
        }
    }
}
