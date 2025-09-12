package dev.lysmux.lab6.client.controller.command;

import dev.lysmux.lab6.client.handler.ScriptHandler;
import dev.lysmux.lab6.common.command.Command;
import dev.lysmux.lab6.common.command.CommandRegistry;
import dev.lysmux.lab6.common.dto.Response;
import dev.lysmux.lab6.common.io.file.FileManager;
import dev.lysmux.lab6.common.io.file.StandartFileManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@Command(name = "execute_script", description = "Executes the script from the specified file")
@RequiredArgsConstructor
final public class ExecuteScriptCommand {
    @NonNull
    private final CommandRegistry commandRegistry;

    private final FileManager fileManager = new StandartFileManager();
    private final Set<Path> runningScripts = new HashSet<>();

    public Response execute(Path scriptPath) {
        if (!scriptPath.toFile().exists()) return Response.builder()
                .text("Path does not exist")
                .success(false)
                .build();
        if (!scriptPath.toFile().isFile()) return Response.builder()
                .text("Path is not a file")
                .success(false)
                .build();
        if (!scriptPath.toFile().canRead()) return Response.builder()
                .text("File is not readable")
                .success(false)
                .build();

        if (!runningScripts.add(scriptPath)) {
            return Response.builder()
                    .text("Recursion detected")
                    .success(false)
                    .build();
        }

        try {
            ScriptHandler scriptHandler = new ScriptHandler(commandRegistry, fileManager.read(scriptPath.toString()));
            Response response = Response.builder()
                    .text(scriptHandler.executeScript())
                    .build();
            runningScripts.remove(scriptPath); // reset after complete
            return response;
        } catch (IOException e) {
            return Response.builder()
                    .text("Could not read script")
                    .success(false)
                    .build();
        }
    }
}
