package vdt.kpimanagement.repository;

import vdt.kpimanagement.entity.KpiTemplate;

import java.util.List;

public interface KpiTemplateRepo extends BaseRepository<KpiTemplate, Long> {
    List<KpiTemplate> findByIsActiveTrueAndIsDeletedFalse();
    List<KpiTemplate> findByCategory_IdAndIsActiveTrueAndIsDeletedFalse(Long categoryId);
    boolean existsByTemplateCodeAndIsDeletedFalse(String templateCode);
}
