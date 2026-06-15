package vdt.kpimanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "kpi_item_evaluations")
@Getter
@Setter
public class KpiItemEvaluation extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kpi_item_id", nullable = false, unique = true)
    private KpiItem kpiItem;

    @Column(name = "self_score", precision = 5, scale = 2)
    private BigDecimal selfScore;

    @Column(name = "self_comment", columnDefinition = "TEXT")
    private String selfComment;

    @Column(name = "self_eval_at")
    private LocalDateTime selfEvalAt;

    @Column(name = "manager_score", precision = 5, scale = 2)
    private BigDecimal managerScore;

    @Column(name = "manager_comment", columnDefinition = "TEXT")
    private String managerComment;

    @Column(name = "manager_eval_at")
    private LocalDateTime managerEvalAt;

    // Điểm chốt cuối cùng (thường = manager_score hoặc do admin quyết)
    @Column(name = "final_score", precision = 5, scale = 2)
    private BigDecimal finalScore;
}
