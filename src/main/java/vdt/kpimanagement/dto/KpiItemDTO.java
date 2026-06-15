package vdt.kpimanagement.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KpiItemDTO {
    Long id;
    Long documentId;
    Long templateId;
    Long parentId;
    Double weight;
    Double targetValue;
    Double currentValue;
    Boolean isDeleted;
}
