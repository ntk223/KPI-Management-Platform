package vdt.kpimanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vdt.kpimanagement.entity.KpiTrackingLog;

import java.util.List;

public interface KpiTrackingLogRepo extends JpaRepository<KpiTrackingLog, Long> {
    List<KpiTrackingLog> findByKpiItem_IdAndIsDeletedFalseOrderByCreatedAtDesc(Long kpiItemId);
}
