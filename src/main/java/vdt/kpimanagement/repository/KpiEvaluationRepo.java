package vdt.kpimanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vdt.kpimanagement.entity.KpiEvaluation;

import java.util.Optional;

public interface KpiEvaluationRepo extends JpaRepository<KpiEvaluation, Long> {
    Optional<KpiEvaluation> findByDocument_IdAndIsDeletedFalse(Long documentId);
}
