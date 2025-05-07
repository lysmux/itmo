package dev.lysmux.lab8.client.ui.serversettings;

import dev.lysmux.lab8.client.APIClient;
import dev.lysmux.lab8.client.ui.scene.SceneManager;
import dev.lysmux.lab8.client.ui.util.FormValidator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerSettingsController implements Initializable {
    @FXML private Label couldNotConnectLabel;
    @FXML private Button connectBtn;
    @FXML private TextField addressField;
    @FXML private TextField portField;

    private APIClient client = APIClient.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        couldNotConnectLabel.setVisible(false);

        FormValidator validator = new FormValidator();

        validator.addValidatorItem(addressField).setRegex("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$");
        validator.addValidatorItem(portField).setMin(0d).setMax(65535d);

        connectBtn.disableProperty().bind(validator.isValid().not());
    }

    public void connect() {
        String address = addressField.getText();
        int port = Integer.parseInt(portField.getText());

        if (!client.connect(address, port)) {
            couldNotConnectLabel.setVisible(true);
            return;
        }

        if (client.isLogin()) SceneManager.getInstance().switchTo("main");
        else SceneManager.getInstance().switchTo("auth");
    }
}
