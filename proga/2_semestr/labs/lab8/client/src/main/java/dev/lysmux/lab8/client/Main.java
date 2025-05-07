package dev.lysmux.lab8.client;

import dev.lysmux.lab8.client.ui.scene.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        APIClient client = APIClient.getInstance();

        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.setStage(stage);

        sceneManager.loadScene("auth", "/ui/auth/auth-view.fxml");
        sceneManager.loadScene("main", "/ui/main/main-view.fxml");
        sceneManager.loadScene("profile", "/ui/profile/profile-view.fxml");
        sceneManager.loadScene("server-settings", "/ui/server-settings/server-settings-view.fxml");

        sceneManager.switchTo("server-settings");

//        if (client.isLogin()) sceneManager.switchTo("main");
//        else sceneManager.switchTo("auth");
    }

    public static void main(String[] args) {
        launch();
    }
}
