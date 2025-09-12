package dev.lysmux.lab5;

import dev.lysmux.lab5.collection.CollectionManager;
import dev.lysmux.lab5.config.Config;
import dev.lysmux.lab5.controller.CommandManager;
import dev.lysmux.lab5.controller.command.*;
import dev.lysmux.lab5.handler.ConsoleHandler;

public class Main {
    public static void main(String[] args) {
        Config config = Config.fromEnv();
        CommandManager commandManager = new CommandManager();
        CollectionManager collectionManager = new CollectionManager(config.collectionPath());
        collectionManager.load();

        commandManager.registerCommand(new ExecuteScriptCommand(commandManager));
        commandManager.registerCommand(new ExitCommand(collectionManager));
        commandManager.registerCommand(new ForceExitCommand(collectionManager));
        commandManager.registerCommand(new HelpCommand(commandManager));

        commandManager.registerCommand(new AddCommand(collectionManager));
        commandManager.registerCommand(new AddRandomCommand(collectionManager));
        commandManager.registerCommand(new AddIfMaxCommand(collectionManager));
        commandManager.registerCommand(new InfoCommand(collectionManager));
        commandManager.registerCommand(new ClearCommand(collectionManager));
        commandManager.registerCommand(new ShowCommand(collectionManager));
        commandManager.registerCommand(new SaveCommand(collectionManager));
        commandManager.registerCommand(new UpdateCommand(collectionManager));
        commandManager.registerCommand(new CountGreaterThanMinimalPointCommand(collectionManager));
        commandManager.registerCommand(new PrintFieldDescendingMinimalPoint(collectionManager));
        commandManager.registerCommand(new RemoveAnyByMinimalPointCommand(collectionManager));
        commandManager.registerCommand(new RemoveByIDCommand(collectionManager));
        commandManager.registerCommand(new RemoveGreaterCommand(collectionManager));
        commandManager.registerCommand(new RemoveLowerCommand(collectionManager));

        commandManager.registerCommand(new RestoreBackupCommand(collectionManager));
        commandManager.registerCommand(new CheckBackupCommand(collectionManager));
        commandManager.registerCommand(new RemoveBackupCommand(collectionManager));

        ConsoleHandler handler = new ConsoleHandler(commandManager);
        handler.run();
    }
}