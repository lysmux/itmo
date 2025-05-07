package dev.lysmux.lab8.client.ui.main;

import dev.lysmux.lab8.client.ui.util.FormValidator;
import dev.lysmux.lab8.common.collection.model.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ItemViewController implements Initializable {
    @FXML
    private TextField nameField;
    @FXML
    private TextField minimalPointField;
    @FXML
    private ChoiceBox<Difficulty> difficultyChoice;
    @FXML
    private TextField coordinateXField;
    @FXML
    private TextField coordinateYField;
    @FXML
    private TextField authorLocationName;
    @FXML
    private TextField authorLocationY;
    @FXML
    private TextField authorLocationX;
    @FXML
    private TextField authorWeightField;
    @FXML
    private DatePicker authorBirthdayField;
    @FXML
    private TextField authorNameField;

    @FXML
    private Button addBtn;

    private final ObjectProperty<LabWork> item = new SimpleObjectProperty<>();
    private final BooleanProperty readOnly = new SimpleBooleanProperty(false);
    private Consumer<LabWork> onComplete;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        difficultyChoice.getItems().addAll(Difficulty.values());

        addBtn.textProperty().bind(Bindings.createStringBinding(() -> item.get() == null
                ? resourceBundle.getString("collection.button.add")
                : resourceBundle.getString("collection.button.save"), item));

        item.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;

            nameField.setText(newValue.getName());
            minimalPointField.setText(Long.toString(newValue.getMinimalPoint()));
            difficultyChoice.getSelectionModel().select(newValue.getDifficulty());
            coordinateXField.setText(newValue.getCoordinates().getX().toString());
            coordinateYField.setText(newValue.getCoordinates().getY().toString());
            authorNameField.setText(newValue.getAuthor().getName());
            authorBirthdayField.setValue(newValue.getAuthor().getBirthday().toLocalDate());
            authorWeightField.setText(Long.toString(newValue.getAuthor().getWeight()));
            authorLocationName.setText(newValue.getAuthor().getLocation().getName());
            authorLocationY.setText(newValue.getAuthor().getLocation().getY().toString());
            authorLocationX.setText(newValue.getAuthor().getLocation().getX().toString());
        });

        readOnly.addListener((observable, oldValue, newValue) -> {
            nameField.setDisable(newValue);
            minimalPointField.setDisable(newValue);
            difficultyChoice.setDisable(newValue);
            coordinateXField.setDisable(newValue);
            coordinateYField.setDisable(newValue);
            authorNameField.setDisable(newValue);
            authorBirthdayField.setDisable(newValue);
            authorWeightField.setDisable(newValue);
            authorLocationName.setDisable(newValue);
            authorLocationY.setDisable(newValue);
            authorLocationX.setDisable(newValue);

            addBtn.setVisible(!newValue);
            addBtn.setManaged(!newValue);
        });

        addBtn.setOnAction(event -> applyChanges());

        FormValidator formValidator = new FormValidator();
        formValidator.addValidatorItem(nameField);
        formValidator.addValidatorItem(minimalPointField).setMin(0d);
        formValidator.addValidatorItem(coordinateXField).setMin(-1000d).setMax(592d);
        formValidator.addValidatorItem(coordinateYField).setMin(-1000d).setMax(892d);
        formValidator.addValidatorItem(authorLocationName);
        formValidator.addValidatorItem(authorLocationY).setMin(-10000d).setMax(10000d);
        formValidator.addValidatorItem(authorLocationX).setMin(-10000d).setMax(10000d);
        formValidator.addValidatorItem(authorWeightField).setMin(0d);
        formValidator.addValidatorItem(authorNameField);
        addBtn.disableProperty().bind(formValidator.isValid().not());

    }

    public void setItem(LabWork item) {
        this.item.set(item);
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly.set(readOnly);
    }

    public void setOnComplete(Consumer<LabWork> onComplete) {
        this.onComplete = onComplete;
    }

    public void applyChanges() {
        LabWork item = this.item.get() != null
                ? this.item.get()
                : new LabWork();

        if (item.getCoordinates() == null) item.setCoordinates(new Coordinates());
        if (item.getAuthor() == null) item.setAuthor(new Person());
        if (item.getAuthor().getLocation() == null) item.getAuthor().setLocation(new Location());

        item.setName(nameField.getText());
        item.setMinimalPoint(Long.parseLong(minimalPointField.getText()));
        item.setDifficulty(difficultyChoice.getValue());
        item.getCoordinates().setX(Integer.parseInt(coordinateXField.getText()));
        item.getCoordinates().setY(Double.parseDouble(coordinateYField.getText()));
        item.getAuthor().setName(authorNameField.getText());
        item.getAuthor().setBirthday(Date.valueOf(authorBirthdayField.getValue()));
        item.getAuthor().setWeight(Long.parseLong(authorWeightField.getText()));
        item.getAuthor().getLocation().setName(authorLocationName.getText());
        item.getAuthor().getLocation().setX(Integer.parseInt(authorLocationX.getText()));
        item.getAuthor().getLocation().setY(Float.parseFloat(authorLocationY.getText()));

        if (onComplete != null) onComplete.accept(item);

        Stage stage = (Stage) addBtn.getScene().getWindow();
        stage.close();
    }
}
