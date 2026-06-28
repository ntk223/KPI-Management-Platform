package vdt.kpimanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vdt.kpimanagement.entity.KpiAttachment;

import java.util.List;

public interface KpiAttachmentRepo extends JpaRepository<KpiAttachment, Long> {
    KpiAttachment findByKpiItem_IdAndIsDeletedFalse(Long kpiItemId);
    List<KpiAttachment> findByKpiItem_IdAndIsDeletedFalse(Long kpiItemId, org.springframework.data.domain.Sort sort);
    List<KpiAttachment> findAllByKpiItem_IdAndIsDeletedFalse(Long kpiItemId);
    List<KpiAttachment> findByKpiItem_IdInAndIsDeletedFalse(List<Long> kpiItemIds);
}
