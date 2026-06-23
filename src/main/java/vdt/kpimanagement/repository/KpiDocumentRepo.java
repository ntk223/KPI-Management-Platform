package vdt.kpimanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vdt.kpimanagement.entity.KpiDocument;
import vdt.kpimanagement.constant.enums.DocumentTargetType;

import java.util.List;
import java.util.Optional;

public interface KpiDocumentRepo extends JpaRepository<KpiDocument, Long> {

    List<KpiDocument> findByCycle_IdAndTargetTypeAndIsDeletedFalse(Long cycleId, DocumentTargetType targetType);

    Optional<KpiDocument> findByCycle_IdAndTargetTypeAndTargetIdAndIsDeletedFalse(
            Long cycleId, DocumentTargetType targetType, Long targetId);

    List<KpiDocument> findByParentDocument_IdAndIsDeletedFalse(Long parentDocId);

    Long countByCycle_IdAndIsDeletedFalse(Long cycleId);
}
