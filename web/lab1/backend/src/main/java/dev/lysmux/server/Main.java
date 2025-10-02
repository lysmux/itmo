package dev.lysmux.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.lysmux.fcgi.AppRouter;
import dev.lysmux.fcgi.FCGI;
import dev.lysmux.server.api.router.DotsRouter;
import dev.lysmux.server.di.CacheModule;
import dev.lysmux.server.di.CheckersModule;
import dev.lysmux.server.di.DatabaseModule;
import dev.lysmux.server.di.RepositoryModule;
import dev.lysmux.server.infra.database.LiquibaseMigration;
import lombok.extern.java.Log;

import java.io.InputStream;
import java.util.logging.LogManager;

@Log
public class Main {
    public static void main(String[] args) throws Exception {
        setupLogger();

        Injector injector = Guice.createInjector(
                new CacheModule(),
                new DatabaseModule(),
                new RepositoryModule(),
                new CheckersModule()
        );

        LiquibaseMigration migration = injector.getInstance(LiquibaseMigration.class);
        migration.runMigrations();

        AppRouter appRouter = new AppRouter();
        appRouter.includeRouter(injector.getInstance(DotsRouter.class));

        FCGI fcgi = new FCGI(appRouter);
        fcgi.run();
    }

    public static void setupLogger() {
        try (InputStream inputStream = Main.class
                .getResourceAsStream("/logging.properties")) {
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (Exception e) {
            log.severe("Could not load log  properties");
        }
    }
}