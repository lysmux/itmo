package dev.lysmux.lab6.client;

import dev.lysmux.lab6.client.config.Config;
import dev.lysmux.lab6.client.controller.ServerCommandWrapper;
import dev.lysmux.lab6.client.controller.command.ExecuteScriptCommand;
import dev.lysmux.lab6.client.controller.command.ExitCommand;
import dev.lysmux.lab6.client.controller.command.HelpCommand;
import dev.lysmux.lab6.client.network.Client;
import dev.lysmux.lab6.common.command.CommandRegistry;
import dev.lysmux.lab6.common.command.meta.CommandInfo;
import dev.lysmux.lab6.common.handler.ConsoleHandler;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Config config = Config.fromArgs();

        CommandRegistry commandRegistry = new CommandRegistry() {
            {
                addCommand(new ExitCommand());
                addCommand(new HelpCommand(this));
                addCommand(new ExecuteScriptCommand(this));
            }
        };

        Client client = new Client(config.host(), config.port(), config.clientMode());
        List<CommandInfo> serverCommands = client.fetchServerCommands();
        for (CommandInfo serverCommand : serverCommands) {
            commandRegistry.addCommand(new ServerCommandWrapper(client, serverCommand));
        }

        ConsoleHandler cli = new ConsoleHandler(commandRegistry);
        cli.run();
    }
}
