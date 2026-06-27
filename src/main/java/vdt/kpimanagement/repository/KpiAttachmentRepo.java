package vdt.kpimanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vdt.kpimanagement.entity.KpiAttachment;

public interface KpiAttachmentRepo extends JpaRepository<KpiAttachment, Long> {
}
