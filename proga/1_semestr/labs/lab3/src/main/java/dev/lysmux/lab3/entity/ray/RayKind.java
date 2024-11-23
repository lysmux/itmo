package dev.lysmux.lab3.entity.ray;

public enum RayKind {
    LIGHT("света"),
    ULTRAVIOLET("ультрафиолетовых"),
    INFRARED("инфракрасных"),
    COSMIC("космических");

    private final String value;

    RayKind(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
