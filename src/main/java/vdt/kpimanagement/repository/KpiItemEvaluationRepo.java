package vdt.kpimanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vdt.kpimanagement.entity.KpiItemEvaluation;

import java.util.Optional;

public interface KpiItemEvaluationRepo extends JpaRepository<KpiItemEvaluation, Long> {
    Optional<KpiItemEvaluation> findByKpiItem_IdAndIsDeletedFalse(Long kpiItemId);
}
