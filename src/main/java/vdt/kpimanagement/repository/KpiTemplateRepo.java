package vdt.kpimanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vdt.kpimanagement.entity.KpiTemplate;

import java.util.List;

public interface KpiTemplateRepo extends JpaRepository<KpiTemplate, Long> {
    List<KpiTemplate> findByIsActiveTrueAndIsDeletedFalse();
    List<KpiTemplate> findByCategory_IdAndIsActiveTrueAndIsDeletedFalse(Long categoryId);
}
