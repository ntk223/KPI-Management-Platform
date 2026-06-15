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
public class KpiItemSearchDTO {
    String keyword;
    Long documentId;
    Long templateId;
    Long parentId;
}
