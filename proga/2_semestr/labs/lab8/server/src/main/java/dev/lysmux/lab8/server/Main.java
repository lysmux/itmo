package dev.lysmux.lab8.server;

import dev.lysmux.lab8.common.command.CommandRegistry;
import dev.lysmux.lab8.common.handler.ConsoleHandler;
import dev.lysmux.lab8.server.collection.CollectionManager;
import dev.lysmux.lab8.server.command.GetCommandsCommand;
import dev.lysmux.lab8.server.command.HelpCommand;
import dev.lysmux.lab8.server.command.admin.ExitCommand;
import dev.lysmux.lab8.server.command.auth.ChangePasswordCommand;
import dev.lysmux.lab8.server.command.auth.GetProfileDataCommand;
import dev.lysmux.lab8.server.command.auth.LoginCommand;
import dev.lysmux.lab8.server.command.auth.RegisterCommand;
import dev.lysmux.lab8.server.command.collection.*;
import dev.lysmux.lab8.server.config.Config;
import dev.lysmux.lab8.server.config.ConfigLoader;
import dev.lysmux.lab8.server.network.Server;
import dev.lysmux.lab8.server.network.TCPServer;
import dev.lysmux.lab8.server.repository.LabWorkRepository;
import dev.lysmux.lab8.server.repository.UserRepository;
import dev.lysmux.lab8.server.service.UserService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        ConfigLoader<Config> configLoader = new ConfigLoader<>(Config.class);
        Config config = configLoader.loadEnv().read("_");

        Connection conn = DriverManager.getConnection(
                config.database().jdbc(),
                config.database().user(),
                config.database().password()
        );

        UserRepository userRepository = new UserRepository(conn);
        LabWorkRepository lwRepository = new LabWorkRepository(conn);

        UserService userService = new UserService(userRepository);

        CollectionManager collectionManager = new CollectionManager(lwRepository);
        collectionManager.load();

        RequestContext requestContext = new RequestContext();

        CommandRegistry commandRegistry = new CommandRegistry() {
            {
                addCommand(new ExitCommand());

                addCommand(new HelpCommand(this));
                addCommand(new AddCommand(collectionManager, requestContext));
                addCommand(new AddRandomCommand(collectionManager, requestContext));
                addCommand(new AddIfMaxCommand(collectionManager, requestContext));
                addCommand(new InfoCommand(collectionManager));
                addCommand(new ClearCommand(collectionManager, requestContext));
                addCommand(new ShowCommand(collectionManager));
                addCommand(new UpdateCommand(collectionManager, requestContext));
                addCommand(new CountGreaterThanMinimalPointCommand(collectionManager));
                addCommand(new PrintFieldDescendingMinimalPoint(collectionManager));
                addCommand(new RemoveAnyByMinimalPointCommand(collectionManager, requestContext));
                addCommand(new RemoveByIDCommand(collectionManager, requestContext));
                addCommand(new RemoveGreaterCommand(collectionManager, requestContext));
                addCommand(new RemoveLowerCommand(collectionManager, requestContext));
                addCommand(new GetCommandsCommand(this));

                addCommand(new LoginCommand(userService));
                addCommand(new RegisterCommand(userService));
                addCommand(new GetProfileDataCommand(requestContext));
                addCommand(new ChangePasswordCommand(userService, requestContext));
            }
        };

        Server server = new TCPServer(config.listenPort(), commandRegistry, userService, requestContext);
        new Thread(server).start();

        ConsoleHandler cli = new ConsoleHandler(commandRegistry);
        cli.run();
    }
}
