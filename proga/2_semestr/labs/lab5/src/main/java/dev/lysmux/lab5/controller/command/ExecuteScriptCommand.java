package dev.lysmux.lab5.controller.command;

import dev.lysmux.lab5.controller.Command;
import dev.lysmux.lab5.controller.CommandManager;
import dev.lysmux.lab5.controller.Response;
import dev.lysmux.lab5.handler.ScriptHandler;
import dev.lysmux.lab5.io.file.FileManager;
import dev.lysmux.lab5.io.file.StandartFileManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@Command(name = "execute_script", description = "Executes the script from the specified file")
@RequiredArgsConstructor
public class ExecuteScriptCommand {
    @NonNull
    private final CommandManager commandManager;

    private final FileManager fileManager = new StandartFileManager();
    private final Set<Path> runningScripts = new HashSet<>();

    public Response execute(Path scriptPath) {
        if (!scriptPath.toFile().exists()) return Response.of("Path does not exist");
        if (!scriptPath.toFile().isFile()) return Response.of("Path is not a file");
        if (!scriptPath.toFile().canRead()) return Response.of("File is not readable");

        if (!runningScripts.add(scriptPath)) {
            return Response.of("Recursion detected");
        }

        try {
            ScriptHandler scriptHandler = new ScriptHandler(commandManager, fileManager.read(scriptPath.toString()));
            Response response = Response.of(scriptHandler.executeScript());
            runningScripts.remove(scriptPath); // reset after complete
            return response;
        } catch (IOException e) {
            return Response.of("Could not read script");
        }
    }
}
