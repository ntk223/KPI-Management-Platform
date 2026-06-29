package vdt.kpimanagement.dto;

import jakarta.validation.constraints.*;
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
    @NotBlank(message = "Mã chu kỳ không được để trống")
    @Size(max = 20, message = "Mã chu kỳ không được vượt quá 20 ký tự")
    private String cycleCode;

    @NotBlank(message = "Tên chu kỳ không được để trống")
    @Size(max = 100, message = "Tên chu kỳ không được vượt quá 100 ký tự")
    private String name;

    @NotNull(message = "Loại chu kỳ không được để trống")
    private CycleType type;

    @NotNull(message = "Ngày bắt đầu là bắt buộc")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc là bắt buộc")
    private LocalDate endDate;
}
