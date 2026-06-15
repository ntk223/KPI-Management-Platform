package vdt.kpimanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vdt.kpimanagement.constant.enums.KpiItemStatus;
import vdt.kpimanagement.constant.enums.TargetType;

import java.math.BigDecimal;

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

    // Tiêu chí cha (phân cấp bên trong 1 phiếu — khác parent_doc_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_item_id")
    private KpiItem parentItem;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "unit", nullable = false, length = 30)
    private String unit;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 20)
    private TargetType targetType;

    // Tổng weight của tất cả items trong 1 document phải = 100
    @Column(name = "weight", nullable = false, precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(name = "target_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal targetValue;

    @Column(name = "current_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal currentValue = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private KpiItemStatus status = KpiItemStatus.ACTIVE;
}
