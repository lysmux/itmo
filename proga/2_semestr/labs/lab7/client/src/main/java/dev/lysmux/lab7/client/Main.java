package dev.lysmux.lab7.client;

import dev.lysmux.lab7.client.config.Config;
import dev.lysmux.lab7.client.controller.ServerCommandWrapper;
import dev.lysmux.lab7.client.controller.command.ExecuteScriptCommand;
import dev.lysmux.lab7.client.controller.command.ExitCommand;
import dev.lysmux.lab7.client.controller.command.HelpCommand;
import dev.lysmux.lab7.client.controller.command.auth.LoginCommand;
import dev.lysmux.lab7.client.controller.command.auth.RegisterCommand;
import dev.lysmux.lab7.client.network.Client;
import dev.lysmux.lab7.common.command.CommandRegistry;
import dev.lysmux.lab7.common.command.meta.CommandInfo;
import dev.lysmux.lab7.common.handler.ConsoleHandler;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Config config = Config.fromArgs();
        AuthContainer authContainer = new AuthContainer();

        Client client = new Client(config.host(), config.port(), config.clientMode());

        CommandRegistry commandRegistry = new CommandRegistry() {
            {
                addCommand(new ExitCommand());
                addCommand(new HelpCommand(this));
                addCommand(new ExecuteScriptCommand(this));

                addCommand(new LoginCommand(authContainer, client));
                addCommand(new RegisterCommand(authContainer, client));
            }
        };

        List<CommandInfo> serverCommands = client.fetchServerCommands();
        for (CommandInfo serverCommand : serverCommands) {
            commandRegistry.addCommand(new ServerCommandWrapper(client, serverCommand, authContainer));
        }

        ConsoleHandler cli = new ConsoleHandler(commandRegistry);
        cli.run();
    }
}
