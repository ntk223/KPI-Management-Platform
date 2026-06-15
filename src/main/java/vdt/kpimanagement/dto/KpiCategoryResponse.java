package vdt.kpimanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KpiCategoryResponse {
    private Long id;
    private String categoryCode;
    private String name;
    private String description;
    private Date createdAt;
    private Date updatedAt;
}
