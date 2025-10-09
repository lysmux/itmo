package dev.lysmux.dto;

import dev.lysmux.domain.DotCheck;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CheckResult {
    private DotCheck[] checks;
    private String time;
    private long executionTime;
}
