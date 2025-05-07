package dev.lysmux.lab8.client.ui.auth;

import dev.lysmux.lab8.client.APIClient;
import dev.lysmux.lab8.client.ui.scene.SceneManager;
import dev.lysmux.lab8.client.ui.util.FormValidator;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class AuthController implements Initializable {
    @FXML
    private VBox rootPane;

    @FXML
    private Label loginTabLabel;
    @FXML
    private Label registerTabLabel;
    @FXML
    private Line modeUnderline;

    @FXML
    private Button authBtn;
    @FXML
    private Label invalidAuthDataLabel;

    private ResourceBundle bundle;
    private final BooleanProperty loginMode = new SimpleBooleanProperty(true);
    private final APIClient client = APIClient.getInstance();

    private TextField loginField;
    private TextField passwordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bundle = resourceBundle;
        invalidAuthDataLabel.setVisible(false);

        loginTabLabel.setOnMouseClicked(this::switchTabHandler);
        registerTabLabel.setOnMouseClicked(this::switchTabHandler);

        authBtn.textProperty().bind(Bindings.createStringBinding(
                () -> loginMode.get()
                        ? resourceBundle.getString("auth.button.login")
                        : resourceBundle.getString("auth.button.register"),
                loginMode
        ));

        loginField = (TextField) rootPane.lookup("#loginField");
        passwordField = (TextField) rootPane.lookup("#passwordHiddenField");

        FormValidator validator = new FormValidator();
        validator.addValidatorItem(loginField);
        validator.addValidatorItem(passwordField);
        authBtn.disableProperty().bind(validator.isValid().not());

        Platform.runLater(() -> updateLinePosition(loginTabLabel));
    }

    public void switchTabHandler(MouseEvent event) {
        boolean isLogin = event.getSource() == loginTabLabel;
        loginMode.set(isLogin);
        invalidAuthDataLabel.setVisible(false);
        animateLineTo((Label) event.getSource());
    }

    private void animateLineTo(Label label) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(100), modeUnderline);
        transition.setToX(label.localToParent(label.getBoundsInLocal()).getMinX());

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(modeUnderline.endXProperty(), modeUnderline.getEndX())),
                new KeyFrame(Duration.millis(100), new KeyValue(modeUnderline.endXProperty(), label.getWidth()))
        );
        new ParallelTransition(transition, timeline).play();
    }

    public void updateLinePosition(Label label) {
        modeUnderline.setStartX(0);
        modeUnderline.setEndX(label.getWidth());
        modeUnderline.setLayoutX(label.localToParent(label.getBoundsInLocal()).getMinX());
    }

    public void auth() {
        String login = loginField.getText();
        String password = passwordField.getText();

        boolean success = loginMode.get()
                ? client.login(login, password)
                : client.register(login, password);

        String errorMessage = loginMode.get()
                ? bundle.getString("auth.label.invalidAuth")
                : bundle.getString("auth.label.accountExists");

        invalidAuthDataLabel.setText(errorMessage);
        invalidAuthDataLabel.setVisible(!success);

        if (success) SceneManager.getInstance().switchTo("main");
    }
}
