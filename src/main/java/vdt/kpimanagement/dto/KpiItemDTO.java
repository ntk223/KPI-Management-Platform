package vdt.kpimanagement.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vdt.kpimanagement.constant.enums.TargetType;
import vdt.kpimanagement.constant.enums.KpiItemType;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KpiItemDTO {
    Long id;
    Long documentId;

    @NotBlank(message = "Tên tiêu chí không được để trống")
    @Size(max = 200, message = "Tên tiêu chí không được vượt quá 200 ký tự")
    String name;

    String description;

    @NotBlank(message = "Đơn vị tính không được để trống")
    @Size(max = 30, message = "Đơn vị tính không được vượt quá 30 ký tự")
    String unit;

    Long templateId;
    KpiItemType itemType;

    @DecimalMin(value = "0.0", message = "Trọng số đóng góp lên cha không được âm")
    @DecimalMax(value = "100.0", message = "Trọng số đóng góp lên cha không được vượt quá 100")
    BigDecimal parentWeight = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Trọng số đóng góp vào phiếu không được âm")
    @DecimalMax(value = "100.0", message = "Trọng số đóng góp vào phiếu không được vượt quá 100")
    BigDecimal documentWeight = BigDecimal.ZERO;

    BigDecimal weight;

    @NotNull(message = "Giá trị mục tiêu không được để trống")
    BigDecimal targetValue;

    BigDecimal currentValue;
    BigDecimal progress;
    Boolean isDeleted;

    @NotNull(message = "Loại mục tiêu không được để trống")
    TargetType targetType;

    BigDecimal selfScore;
    BigDecimal managerScore;
    BigDecimal finalScore;
}

