package vdt.kpimanagement.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vdt.kpimanagement.constant.enums.TargetType;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KpiItemDTO {
    Long id;
    Long documentId;
    String name;
    String description;
    String unit;
    Long templateId;
    BigDecimal weight;
    BigDecimal targetValue;
    BigDecimal currentValue;
    Boolean isDeleted;
    TargetType targetType;
    BigDecimal selfScore;
    BigDecimal managerScore;
    BigDecimal finalScore;
}
