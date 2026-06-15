package vdt.kpimanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KpiCategoryRequest {
    private String categoryCode;
    private String name;
    private String description;
}
