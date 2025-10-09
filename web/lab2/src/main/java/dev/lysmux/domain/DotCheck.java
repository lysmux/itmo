package dev.lysmux.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DotCheck {
    private double x;
    private double y;
    private double r;
    private boolean contains;
}
