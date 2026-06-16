package vdt.kpimanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vdt.kpimanagement.entity.KpiDocumentEvaluation;

import java.util.Optional;

public interface KpiDocumentEvaluationRepo extends JpaRepository<KpiDocumentEvaluation, Long> {
    Optional<KpiDocumentEvaluation> findByDocument_IdAndIsDeletedFalse(Long documentId);
}
