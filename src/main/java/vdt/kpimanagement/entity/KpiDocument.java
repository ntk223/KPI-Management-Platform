package vdt.kpimanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vdt.kpimanagement.constant.enums.DocumentStatus;
import vdt.kpimanagement.constant.enums.DocumentTargetType;
import vdt.kpimanagement.constant.enums.SourceType;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "kpi_documents",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_doc_target_cycle",
        columnNames = {"cycle_id", "target_type", "target_id"}
    )
)
@Getter
@Setter
public class KpiDocument extends BaseEntity {

    @Column(name = "document_code", nullable = false, length = 100, unique = true)
    private String documentCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycle_id", nullable = false)
    private KpiCycle cycle;

    // Polymorphic: xác định KPI giao cho ai
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 20)
    private DocumentTargetType targetType;

    // NULL nếu targetType = COMPANY | departments.id | employees.id
    // Không đặt FK constraint vì polymorphic — validate ở tầng Service
    @Column(name = "target_id")
    private Long targetId;

    // KPI cha trong cascade (null = gốc cascade)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_doc_id")
    private KpiDocument parentDocument;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false, length = 20)
    private SourceType sourceType = SourceType.ASSIGNED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private Employee createdBy;

    // NULL khi còn DRAFT/PROPOSED chưa có người duyệt
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private Employee approver;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private DocumentStatus status = DocumentStatus.DRAFT;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;
}