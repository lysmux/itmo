package dev.lysmux.lab6.common.collection.validator;

import dev.lysmux.lab6.common.collection.validator.annotations.*;
import dev.lysmux.lab6.common.collection.validator.validators.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class that validates fields and models based on the constraint annotations
 */
public class Validator {
    private static final Map<Class<? extends Annotation>, FieldValidator<? extends Annotation>> validators = new HashMap<>();

    /**
     * Validates model
     *
     * @param model model to validate
     * @throws ValidationException if model has the constraint violations
     */
    public static void validateModel(Object model) throws ValidationException {
        Set<ConstraintViolation> violations = new HashSet<>();

        var modelClass = model.getClass();
        var modelFields = modelClass.getDeclaredFields();
        for (var field : modelFields) {
            field.setAccessible(true);
            try {
                Object value = field.get(model);
                Set<ConstraintViolation> fieldViolations = collectViolations(field, value);
                violations.addAll(fieldViolations);
            } catch (IllegalAccessException ignored) {
            }
        }

        if (!violations.isEmpty()) throw new ValidationException(violations);
    }

    /**
     * Validates field
     *
     * @param field field to validate
     * @param value value to validate
     * @throws ValidationException if field has the constraint violations
     */
    public static void validateField(Field field, Object value) throws ValidationException {
        Set<ConstraintViolation> violations = collectViolations(field, value);
        if (!violations.isEmpty()) throw new ValidationException(violations);
    }

    /**
     * Collect constraint violations
     *
     * @param field field to validate
     * @param value value to validate
     * @return constraint violations
     */
    private static Set<ConstraintViolation> collectViolations(Field field, Object value) {
        Set<ConstraintViolation> violations = new HashSet<>();

        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            @SuppressWarnings("unchecked")
            FieldValidator<Annotation> validator = (FieldValidator<Annotation>) validators.get(annotation.annotationType());
            if (validator != null && !(validator.validate(annotation, value))) {
                violations.add(new ConstraintViolation(
                        validator.getErrorMessage(annotation),
                        value,
                        field.getName()
                ));
            }
        }

        return violations;
    }

    static {
        validators.put(NotNull.class, new NotNullValidator());
        validators.put(NotEmpty.class, new NotEmptyValidator());
        validators.put(Max.class, new MaxValidator());
        validators.put(Min.class, new MinValidator());
        validators.put(MinLength.class, new MinLengthValidator());
        validators.put(MaxLength.class, new MaxLengthValidator());
    }
}


