package dev.lysmux.lab8.client.ui.components;

import dev.lysmux.lab8.client.APIClient;
import dev.lysmux.lab8.client.ui.scene.SceneManager;
import dev.lysmux.lab8.client.ui.scene.Theme;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class HeaderController implements Initializable {
    @FXML private MenuButton profileBtn;
    @FXML private HBox rootPane;

    private final APIClient client = APIClient.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene == null) return;

            newScene.windowProperty().addListener((obs, oldWindow, newWindow) -> {
                if (newWindow == null) return;

                profileBtn.getItems().stream()
                        .filter(item -> item.getStyleClass().contains("login-visible"))
                        .forEach(item -> item.setVisible(client.isLogin()));
            });
        });
    }

    public void logout() {
        client.logout();
        SceneManager.getInstance().switchTo("auth");
    }

    public void switchToProfile() {
        SceneManager.getInstance().switchTo("profile");
    }

    public void changeServer() {
        SceneManager.getInstance().switchTo("server-settings");
    }

    public void changeLanguage(ActionEvent event) {
        MenuItem selectedItem = (MenuItem) event.getSource();
        String languageTag = (String) selectedItem.getUserData();

        Locale locale = Locale.forLanguageTag(languageTag);
        SceneManager.getInstance().switchLocale(locale);
    }

    public void changeTheme(ActionEvent event) {
        MenuItem selectedItem = (MenuItem) event.getSource();
        String themeTag = (String) selectedItem.getUserData();

        Theme theme = Theme.valueOf(themeTag);
        SceneManager.getInstance().switchTheme(theme);
    }
}
