package vdt.kpimanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vdt.kpimanagement.entity.KpiItem;

import java.util.List;

public interface KpiItemRepo extends JpaRepository<KpiItem, Long> {
    List<KpiItem> findByDocument_IdAndIsDeletedFalse(Long documentId);
    List<KpiItem> findByDocumentIdInAndIsDeletedFalse(List<Long> documentIds);
    List<KpiItem> findByParent_IdAndIsDeletedFalse(Long parentId);
    List<KpiItem> findByDocument_IdAndIsDeletedFalseAndParentIsNull(Long documentId);
}
