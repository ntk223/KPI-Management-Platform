package vdt.kpimanagement.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PositionRequest {
    @Size(max = 20, message = "Mã chức vụ không được vượt quá 20 ký tự")
    private String positionCode;

    @NotBlank(message = "Tên chức vụ không được để trống")
    @Size(max = 100, message = "Tên chức vụ không được vượt quá 100 ký tự")
    private String title;

    @NotNull(message = "Cấp bậc không được để trống")
    @Min(value = 1, message = "Cấp bậc phải lớn hơn hoặc bằng 1")
    private Integer level;
}
