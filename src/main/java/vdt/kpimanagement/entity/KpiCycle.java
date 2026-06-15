package vdt.kpimanagement.entity;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "kpi_cycles")
public class KpiCycle {
@jakarta.persistence.Id
@jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@jakarta.persistence.Column(name = "id", nullable = false)
private java.lang.Long id;

@jakarta.persistence.Column(name = "cycle_code", nullable = false, length = 50)
private java.lang.String cycleCode;

@jakarta.persistence.Column(name = "name", nullable = false, length = 100)
private java.lang.String name;

@jakarta.persistence.Column(name = "type", nullable = false, length = 20)
private java.lang.String type;

@jakarta.persistence.Column(name = "start_date", nullable = false)
private java.time.LocalDate startDate;

@jakarta.persistence.Column(name = "end_date", nullable = false)
private java.time.LocalDate endDate;

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