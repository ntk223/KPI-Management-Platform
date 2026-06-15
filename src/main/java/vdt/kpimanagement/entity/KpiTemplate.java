package vdt.kpimanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vdt.kpimanagement.constant.enums.TargetType;

import java.math.BigDecimal;

@Entity
@Table(name = "kpi_templates")
@Getter
@Setter
public class KpiTemplate extends BaseEntity {

    @Column(name = "template_code", nullable = false, length = 50, unique = true)
    private String templateCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private KpiCategory category;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "unit", nullable = false, length = 30)
    private String unit;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 20)
    private TargetType targetType;

    @Column(name = "default_weight", precision = 5, scale = 2)
    private BigDecimal defaultWeight;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}