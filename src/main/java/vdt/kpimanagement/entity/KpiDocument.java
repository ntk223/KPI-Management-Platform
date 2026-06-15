package vdt.kpimanagement.entity;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "kpi_documents")
public class KpiDocument {
@jakarta.persistence.Id
@jakarta.persistence.Column(name = "id", nullable = false)
private java.lang.Long id;

@jakarta.persistence.Column(name = "document_code", nullable = false, length = 100)
private java.lang.String documentCode;

@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
@jakarta.persistence.JoinColumn(name = "cycle_id", nullable = false)
private vdt.kpimanagement.entity.KpiCycle cycle;

@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
@jakarta.persistence.JoinColumn(name = "employee_id", nullable = false)
private vdt.kpimanagement.entity.Employee employee;

@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
@jakarta.persistence.JoinColumn(name = "approver_id", nullable = false)
private vdt.kpimanagement.entity.Employee approver;

@org.hibernate.annotations.ColumnDefault("'DRAFT'")
@jakarta.persistence.Column(name = "status", length = 30)
private java.lang.String status;

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