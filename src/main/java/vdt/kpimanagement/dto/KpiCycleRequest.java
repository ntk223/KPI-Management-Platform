package vdt.kpimanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vdt.kpimanagement.constant.enums.CycleType;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KpiCycleRequest {
    private String cycleCode;
    private String name;
    private CycleType type;
    private LocalDate startDate;
    private LocalDate endDate;
}
