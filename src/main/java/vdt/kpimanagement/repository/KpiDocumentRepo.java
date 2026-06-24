package vdt.kpimanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vdt.kpimanagement.constant.enums.DocumentStatus;
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

    @Query("""
        SELECT d FROM KpiDocument d
        WHERE d.isDeleted = false
          AND (:documentCode IS NULL OR LOWER(d.documentCode) LIKE LOWER(CONCAT('%', :documentCode, '%')))
          AND (:cycleId IS NULL OR d.cycle.id = :cycleId)
          AND (:targetType IS NULL OR d.targetType = :targetType)
          AND (:targetId IS NULL OR d.targetId = :targetId)
          AND (:status IS NULL OR d.status = :status)
    """)
    List<KpiDocument> searchDocuments(
            @Param("documentCode") String documentCode,
            @Param("cycleId") Long cycleId,
            @Param("targetType") DocumentTargetType targetType,
            @Param("targetId") Long targetId,
            @Param("status") DocumentStatus status
    );
}
