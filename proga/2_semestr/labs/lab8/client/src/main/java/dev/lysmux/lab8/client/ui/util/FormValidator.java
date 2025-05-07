package dev.lysmux.lab8.client.ui.util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class FormValidator {
    private final BooleanProperty valid = new SimpleBooleanProperty(false);
    private final List<ValidatorItem> validatorItems = new ArrayList<>();

    public static class ValidatorItem {
        private Double min, max;
        private String regex;
        private final TextField field;

        public ValidatorItem(TextField field) {
            this.field = field;
        }

        public boolean validate() {
            String value = field.getText();
            if (value == null || value.isEmpty()) return false;

            if (regex != null && !value.matches(regex)) return false;

            if (min != null || max != null) {
                try {
                    double number = Double.parseDouble(value);
                    if (min != null && number < min) return false;
                    if (max != null && number > max) return false;
                } catch (NumberFormatException e) {
                    return false;
                }
            }

            return true;
        }

        public void updateStyle(boolean isValid) {
            field.getStyleClass().remove("errorField");
            if (!isValid) {
                field.getStyleClass().add("errorField");
            }
        }

        public ValidatorItem setRegex(String regex) {
            this.regex = regex;
            return this;
        }

        public ValidatorItem setMin(Double min) {
            this.min = min;
            return this;
        }

        public ValidatorItem setMax(Double max) {
            this.max = max;
            return this;
        }
    }

    public ValidatorItem addValidatorItem(TextField field) {
        ValidatorItem item = new ValidatorItem(field);
        validatorItems.add(item);

        field.textProperty().addListener((obs, oldVal, newVal) -> {
            boolean isValid = item.validate();
            item.updateStyle(isValid);
            validateAll();
        });

        return item;
    }

    private void validateAll() {
        valid.set(validatorItems.stream().allMatch(ValidatorItem::validate));
    }

    public BooleanProperty isValid() {
        return valid;
    }
}
