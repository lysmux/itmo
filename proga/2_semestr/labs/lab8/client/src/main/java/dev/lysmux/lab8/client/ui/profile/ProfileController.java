package dev.lysmux.lab8.client.ui.profile;

import dev.lysmux.lab8.client.APIClient;
import dev.lysmux.lab8.client.ui.scene.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML private Button goMainBtn;
    @FXML private VBox rootPane;

    @FXML private Label idLabel;
    @FXML private Label itemsCountLabel;
    @FXML private Label loginDateLabel;
    @FXML private Label registrationDateLabel;

    private APIClient client = APIClient.getInstance();
    private ResourceBundle bundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bundle = resourceBundle;

        rootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene == null) return;

            newScene.windowProperty().addListener((obs, oldWindow, newWindow) -> {
                if (newWindow == null) return;

                updateStat();
            });
        });

        goMainBtn.setOnAction(event -> {
            SceneManager.getInstance().switchTo("main");
        });
    }

    public void changePassword() {
        String password = ((TextField) rootPane.lookup("#passwordHiddenField")).getText();
        client.changePassword(password);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(bundle.getString("profile.changePwd.title"));
        alert.setHeaderText(null);
        alert.setContentText(bundle.getString("profile.changePwd.success"));
        alert.show();
    }

    public void updateStat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        idLabel.setText(Integer.toString(client.getProfileData().id()));
        registrationDateLabel.setText(client.getProfileData().registrationDate().format(formatter));
        loginDateLabel.setText(client.getProfileData().loginDate().format(formatter));
        itemsCountLabel.setText(String.valueOf(client.getOwnedItemsCount()));
    }
}
