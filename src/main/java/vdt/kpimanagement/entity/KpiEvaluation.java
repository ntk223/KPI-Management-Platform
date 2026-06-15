package vdt.kpimanagement.entity;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "kpi_evaluations")
public class KpiEvaluation {
@jakarta.persistence.Id
@jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@jakarta.persistence.Column(name = "id", nullable = false)
private java.lang.Long id;

@jakarta.persistence.OneToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
@jakarta.persistence.JoinColumn(name = "document_id", nullable = false)
private vdt.kpimanagement.entity.KpiDocument document;

@jakarta.persistence.Column(name = "self_score", precision = 5, scale = 2)
private java.math.BigDecimal selfScore;

@jakarta.persistence.Lob
@jakarta.persistence.Column(name = "self_comment")
private java.lang.String selfComment;

@jakarta.persistence.Column(name = "manager_score", precision = 5, scale = 2)
private java.math.BigDecimal managerScore;

@jakarta.persistence.Lob
@jakarta.persistence.Column(name = "manager_comment")
private java.lang.String managerComment;

@jakarta.persistence.Column(name = "final_score", precision = 5, scale = 2)
private java.math.BigDecimal finalScore;

@org.hibernate.annotations.ColumnDefault("0")
@jakarta.persistence.Column(name = "is_deleted")
private java.lang.Boolean isDeleted;

@org.hibernate.annotations.ColumnDefault("CURRENT_TIMESTAMP")
@jakarta.persistence.Column(name = "created_at")
private java.time.Instant createdAt;

@org.hibernate.annotations.ColumnDefault("CURRENT_TIMESTAMP")
@jakarta.persistence.Column(name = "updated_at")
private java.time.Instant updatedAt;



}