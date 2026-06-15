package vdt.kpimanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "kpi_tracking_logs")
@Getter
@Setter
public class KpiTrackingLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kpi_item_id", nullable = false)
    private KpiItem kpiItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private Employee reporter;

    // Audit trail đầy đủ: trước, delta, sau
    @Column(name = "value_before", nullable = false, precision = 15, scale = 2)
    private BigDecimal valueBefore;

    @Column(name = "value_delta", nullable = false, precision = 15, scale = 2)
    private BigDecimal valueDelta;

    @Column(name = "value_after", nullable = false, precision = 15, scale = 2)
    private BigDecimal valueAfter;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}