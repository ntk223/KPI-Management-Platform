package vdt.kpimanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vdt.kpimanagement.constant.enums.CycleStatus;
import vdt.kpimanagement.constant.enums.CycleType;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KpiCycleResponse {
    private Long id;
    private String cycleCode;
    private String name;
    private CycleType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private CycleStatus status;
    private Long createdById;
    private String createdByName;
    private Date createdAt;
    private Date updatedAt;
}
