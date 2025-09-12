package dev.lysmux.lab5.collection.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.lysmux.lab5.collection.validator.annotations.Min;
import dev.lysmux.lab5.collection.validator.annotations.NotEmpty;
import dev.lysmux.lab5.collection.validator.annotations.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Person class
 *
 * @see Location
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    /**
     * Person name
     * <p>
     * Field can not be {@code null} or {@code empty}
     * </p>
     */
    @NotNull
    @NotEmpty
    private String name;

    /**
     * Person birthday
     * <p>Field can not be {@code null}</p>
     */
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date birthday;

    /**
     * Person weight
     * <p>
     * Field can not be {@code null} and must be greater than {@code 0}
     * </p>
     */
    @NotNull
    @Min(0)
    private Long weight;

    /**
     * Person location
     * <p>Field can not be {@code null}</p>
     *
     * @see Location
     */
    @NotNull
    private Location location;
}
