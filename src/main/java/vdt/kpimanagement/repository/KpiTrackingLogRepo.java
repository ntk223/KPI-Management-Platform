package vdt.kpimanagement.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vdt.kpimanagement.entity.KpiTrackingLog;

import java.util.List;

public interface KpiTrackingLogRepo extends JpaRepository<KpiTrackingLog, Long> {
    List<KpiTrackingLog> findByKpiItem_IdAndIsDeletedFalseOrderByCreatedAtDesc(Long kpiItemId);
    List<KpiTrackingLog> findByReporter_IdAndIsDeletedFalseOrderByIdDesc(Long employeeId, Pageable pageable);
    List<KpiTrackingLog> findByReporter_Department_IdAndIsDeletedFalseOrderByIdDesc(Long departmentId, Pageable pageable);
    List<KpiTrackingLog> findByIsDeletedFalseOrderByIdDesc(Pageable pageable);
}
