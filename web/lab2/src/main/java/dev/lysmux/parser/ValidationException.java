package dev.lysmux.parser;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationException extends ParseException {
    public ValidationException(Map<String, String[]> fieldViolations) {
        super("Could not validate object", fieldViolations);
    }

    public static <T> ValidationException fromConstraintViolation(Set<ConstraintViolation<T>> violations) {
        Map<String, String[]> fieldViolations = new HashMap<>();
        for (ConstraintViolation<T> violation : violations) {
            String key = violation.getPropertyPath().toString();
            fieldViolations.computeIfAbsent(key, k -> new String[0]);

            String[] oldArray = fieldViolations.get(key);
            String[] newArray = Arrays.copyOf(oldArray, oldArray.length + 1);
            newArray[oldArray.length] = violation.getMessage();
            fieldViolations.put(key, newArray);
        }


        return new ValidationException(fieldViolations);
    }
}
