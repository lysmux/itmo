package dev.lysmux.lab6.server;

import dev.lysmux.lab6.common.command.CommandRegistry;
import dev.lysmux.lab6.common.handler.ConsoleHandler;
import dev.lysmux.lab6.server.collection.CollectionManager;
import dev.lysmux.lab6.server.controller.command.admin.*;
import dev.lysmux.lab6.server.config.Config;
import dev.lysmux.lab6.server.config.ServerMode;
import dev.lysmux.lab6.server.controller.command.*;
import dev.lysmux.lab6.server.network.Server;
import dev.lysmux.lab6.server.network.TCPServer;
import dev.lysmux.lab6.server.network.UDPServer;

public class Main {
    public static void main(String[] args) {
        Config config = Config.fromEnv();
        CollectionManager collectionManager = new CollectionManager(config.collectionPath());
        collectionManager.load();

        CommandRegistry commandRegistry = new CommandRegistry() {
            {
                addCommand(new HelpCommand(this));
                addCommand(new AddCommand(collectionManager));
                addCommand(new AddRandomCommand(collectionManager));
                addCommand(new AddIfMaxCommand(collectionManager));
                addCommand(new InfoCommand(collectionManager));
                addCommand(new ClearCommand(collectionManager));
                addCommand(new ShowCommand(collectionManager));
                addCommand(new UpdateCommand(collectionManager));
                addCommand(new CountGreaterThanMinimalPointCommand(collectionManager));
                addCommand(new PrintFieldDescendingMinimalPoint(collectionManager));
                addCommand(new RemoveAnyByMinimalPointCommand(collectionManager));
                addCommand(new RemoveByIDCommand(collectionManager));
                addCommand(new RemoveGreaterCommand(collectionManager));
                addCommand(new RemoveLowerCommand(collectionManager));
                addCommand(new GetCommandsCommand(this));

                addCommand(new ExitCommand(collectionManager));
                addCommand(new ForceExitCommand(collectionManager));
                addCommand(new SaveCommand(collectionManager));
                addCommand(new CheckBackupCommand(collectionManager));
                addCommand(new RemoveBackupCommand(collectionManager));
                addCommand(new RestoreBackupCommand(collectionManager));
            }
        };

        Server server;
        if (config.serverMode() == ServerMode.TCP) {
            server = new TCPServer(config.listenPort(), commandRegistry);
        } else {
            server = new UDPServer(config.listenPort(), commandRegistry);
        }
        new Thread(server).start();

        ConsoleHandler cli = new ConsoleHandler(commandRegistry);
        cli.run();
    }
}