package vdt.kpimanagement.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KpiTrackingLogRequestDTO {
    @NotNull(message = "ID tiêu chí KPI là bắt buộc")
    private Long kpiItemId;

    @NotNull(message = "ID người báo cáo là bắt buộc")
    private Long reporterId;

    @NotNull(message = "Giá trị hiện tại là bắt buộc")
    private BigDecimal currentValue; // new absolute current value

    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    private String notes;
}
