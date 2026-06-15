package vdt.kpimanagement.entity;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "kpi_tracking_logs")
public class KpiTrackingLog {
@jakarta.persistence.Id
@jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@jakarta.persistence.Column(name = "id", nullable = false)
private java.lang.Long id;

@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
@jakarta.persistence.JoinColumn(name = "kpi_item_id", nullable = false)
private vdt.kpimanagement.entity.KpiItem kpiItem;

@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
@jakarta.persistence.JoinColumn(name = "reporter_id", nullable = false)
private vdt.kpimanagement.entity.Employee reporter;

@jakarta.persistence.Column(name = "value_delta", nullable = false, precision = 15, scale = 2)
private java.math.BigDecimal valueDelta;

@jakarta.persistence.Column(name = "evidence_url")
private java.lang.String evidenceUrl;

@jakarta.persistence.Lob
@jakarta.persistence.Column(name = "notes")
private java.lang.String notes;

@jakarta.persistence.Column(name = "source_type", nullable = false, length = 20)
private java.lang.String sourceType;

@org.hibernate.annotations.ColumnDefault("CURRENT_TIMESTAMP")
@jakarta.persistence.Column(name = "created_at")
private java.time.Instant createdAt;



}