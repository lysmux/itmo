package dev.lysmux.lab5.collection.model;

import dev.lysmux.lab5.collection.validator.annotations.Max;
import dev.lysmux.lab5.collection.validator.annotations.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Coordinates class
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coordinates implements Comparable<Coordinates> {
    /**
     * X coordinate
     * <p>
     * Field can not be {@code null} and must be less than or equal to {@code 592}
     * </p>
     */
    @NotNull
    @Max(592)
    private Integer x;

    /**
     * X coordinate
     * <p>
     * Field can not be {@code null} and must be less than or equal to {@code 892}
     * </p>
     */
    @NotNull
    @Max(892)
    private Double y;

    @Override
    public int compareTo(Coordinates o) {
        if (x.compareTo(o.x) != 0) return x.compareTo(o.x);
        return y.compareTo(o.y);
    }
}
