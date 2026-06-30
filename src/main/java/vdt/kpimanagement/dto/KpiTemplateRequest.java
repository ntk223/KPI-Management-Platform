package vdt.kpimanagement.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vdt.kpimanagement.constant.enums.TargetType;
import vdt.kpimanagement.constant.enums.KpiItemType;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KpiTemplateRequest {
    @Size(max = 50, message = "Mã mẫu KPI không được vượt quá 50 ký tự")
    private String templateCode;

    @NotNull(message = "Danh mục KPI không được để trống")
    private Long categoryId;

    @NotBlank(message = "Tên mẫu KPI không được để trống")
    @Size(max = 200, message = "Tên mẫu KPI không được vượt quá 200 ký tự")
    private String name;

    private String description;

    @NotBlank(message = "Đơn vị tính không được để trống")
    @Size(max = 30, message = "Đơn vị tính không được vượt quá 30 ký tự")
    private String unit;

    @NotNull(message = "Loại mục tiêu không được để trống")
    private TargetType targetType;

    private KpiItemType itemType = KpiItemType.PERCENTAGE;

    @NotNull(message = "Trọng số mặc định không được để trống")
    @DecimalMin(value = "0.0", message = "Trọng số mặc định không được âm")
    @DecimalMax(value = "100.0", message = "Trọng số mặc định không được vượt quá 100")
    private BigDecimal defaultWeight;
}
