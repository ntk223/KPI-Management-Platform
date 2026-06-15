package vdt.kpimanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "kpi_attachments")
@Getter
@Setter
public class KpiAttachment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tracking_log_id", nullable = false)
    private KpiTrackingLog trackingLog;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    // URL trỏ tới storage (S3/MinIO/local) — không lưu binary trong DB
    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    @Column(name = "file_type", length = 50)
    private String fileType;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private Employee uploadedBy;
}
