package vdt.kpimanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KpiTrackingLogRequestDTO {
    private Long kpiItemId;
    private Long reporterId;
    private BigDecimal currentValue; // new absolute current value
    private String notes;
}
