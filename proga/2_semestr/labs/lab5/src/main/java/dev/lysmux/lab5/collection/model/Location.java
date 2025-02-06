package dev.lysmux.lab5.collection.model;

import dev.lysmux.lab5.collection.validator.annotations.MaxLength;
import dev.lysmux.lab5.collection.validator.annotations.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Location class
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    /**
     * X coordinate
     * <p>Field can not be {@code null}</p>
     */
    @NotNull
    private Integer x;

    /**
     * Y coordinate
     * <p>Field can not be {@code null}</p>
     */
    @NotNull
    private Float y;


    /**
     * Location name
     * <p>
     * Field can not be {@code null} and its length must be less than or equal to {@code 592}
     * </p>
     */
    @NotNull
    @MaxLength(783)
    private String name;
}
