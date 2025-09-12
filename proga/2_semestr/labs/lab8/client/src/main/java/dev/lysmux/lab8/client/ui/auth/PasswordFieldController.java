package dev.lysmux.lab8.client.ui.auth;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class PasswordFieldController implements Initializable {
    @FXML private TextField passwordVisibleField;
    @FXML private PasswordField passwordHiddenField;
    @FXML private Button toggleVisibleBtn;

    private final BooleanProperty passwordVisibleProperty = new SimpleBooleanProperty();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passwordVisibleField.textProperty().bindBidirectional(passwordHiddenField.textProperty());

        passwordVisibleField.managedProperty().bind(passwordVisibleProperty);
        passwordVisibleField.visibleProperty().bind(passwordVisibleProperty);

        passwordHiddenField.visibleProperty().bind(passwordVisibleProperty.not());
        passwordHiddenField.managedProperty().bind(passwordVisibleProperty.not());

        passwordVisibleProperty.addListener((obs, oldVal, isVisible) -> {
            toggleVisibleBtn.getStyleClass().removeAll("visible", "hidden");
            toggleVisibleBtn.getStyleClass().add(isVisible ? "visible" : "hidden");
        });
        toggleVisibleBtn.setOnAction(e -> {passwordVisibleProperty.set(!passwordVisibleProperty.get());});
    }
}
