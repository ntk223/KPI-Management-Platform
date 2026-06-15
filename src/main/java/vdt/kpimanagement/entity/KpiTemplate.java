package vdt.kpimanagement.entity;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "kpi_templates")
public class KpiTemplate {
@jakarta.persistence.Id
@jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@jakarta.persistence.Column(name = "id", nullable = false)
private java.lang.Long id;

@jakarta.persistence.Column(name = "template_code", nullable = false, length = 50)
private java.lang.String templateCode;

@jakarta.persistence.Column(name = "name", nullable = false, length = 150)
private java.lang.String name;

@jakarta.persistence.Column(name = "unit", nullable = false, length = 20)
private java.lang.String unit;

@jakarta.persistence.Column(name = "target_type", nullable = false, length = 20)
private java.lang.String targetType;

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