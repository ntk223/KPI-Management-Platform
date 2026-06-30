package vdt.kpimanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vdt.kpimanagement.constant.enums.KpiItemStatus;
import vdt.kpimanagement.constant.enums.TargetType;
import vdt.kpimanagement.constant.enums.KpiItemType;
import vdt.kpimanagement.constant.enums.AggregationType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "kpi_items")
@Getter
@Setter
public class KpiItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private KpiDocument document;

    // NULL nếu tiêu chí được tạo thủ công, không theo template
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private KpiTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private KpiItem parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<KpiItem> children = new ArrayList<>();

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "unit", nullable = false, length = 30)
    private String unit;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 20)
    private TargetType targetType;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false, length = 20)
    private KpiItemType itemType = KpiItemType.PERCENTAGE;

    @Enumerated(EnumType.STRING)
    @Column(name = "aggregation_type", length = 20)
    private AggregationType aggregationType;

    @Column(name = "parent_weight", nullable = false, precision = 5, scale = 2)
    private BigDecimal parentWeight = BigDecimal.ZERO;

    @Column(name = "document_weight", nullable = false, precision = 5, scale = 2)
    private BigDecimal documentWeight = BigDecimal.ZERO;

    @Column(name = "target_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal targetValue = BigDecimal.ZERO;

    @Column(name = "current_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal currentValue = BigDecimal.ZERO;

    // Tiến độ tự tính/cập nhật (%)
    @Column(name = "progress", nullable = false, precision = 5, scale = 2)
    private BigDecimal progress = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private KpiItemStatus status = KpiItemStatus.ACTIVE;
}
