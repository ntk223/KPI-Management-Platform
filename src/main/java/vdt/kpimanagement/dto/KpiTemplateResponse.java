package vdt.kpimanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vdt.kpimanagement.constant.enums.TargetType;
import vdt.kpimanagement.constant.enums.KpiItemType;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KpiTemplateResponse {
    private Long id;
    private String templateCode;
    private Long categoryId;
    private String categoryName;
    private String name;
    private String description;
    private String unit;
    private TargetType targetType;
    private KpiItemType itemType;
    private BigDecimal defaultWeight;
    private boolean isActive;
    private Date createdAt;
    private Date updatedAt;
}
