package dev.lysmux.lab3.entity.ray;

public enum VisibilityType {
    HIDDEN("невидимые"),
    VISIBLE("видимые"),
    NONE("");

    private final String value;

    VisibilityType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
