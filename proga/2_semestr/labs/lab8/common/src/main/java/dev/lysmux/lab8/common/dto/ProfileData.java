package dev.lysmux.lab8.common.dto;

import java.io.Serializable;
import java.time.LocalDate;

public record ProfileData(int id, LocalDate registrationDate, LocalDate loginDate) implements Serializable {
}
