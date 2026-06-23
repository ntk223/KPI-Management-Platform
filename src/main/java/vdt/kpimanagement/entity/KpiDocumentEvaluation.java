package vdt.kpimanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vdt.kpimanagement.constant.enums.EvaluationRating;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "kpi_document_evaluations")
@Getter
@Setter
public class KpiDocumentEvaluation extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false, unique = true)
    private KpiDocument document;

    @Column(name = "total_self_score", precision = 5, scale = 2)
    private BigDecimal totalSelfScore;

    @Column(name = "total_manager_score", precision = 5, scale = 2)
    private BigDecimal totalManagerScore;

    @Column(name = "total_final_score", precision = 5, scale = 2)
    private BigDecimal totalFinalScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "rating", length = 20)
    private EvaluationRating rating;

    @Column(name = "evaluated_by", length = 20)
    private String evaluatedBy;

    @Column(name = "evaluated_at")
    private LocalDateTime evaluatedAt;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
}