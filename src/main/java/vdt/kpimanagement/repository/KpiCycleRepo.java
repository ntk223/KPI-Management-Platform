package vdt.kpimanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vdt.kpimanagement.entity.KpiCycle;
import vdt.kpimanagement.constant.enums.CycleStatus;

import java.util.List;
import java.util.Optional;

public interface KpiCycleRepo extends BaseRepository<KpiCycle, Long> {
    List<KpiCycle> findByStatusAndIsDeletedFalse(CycleStatus status);
    Optional<KpiCycle> findByCycleCodeAndIsDeletedFalse(String cycleCode);

    @Query("""
            SELECT c FROM KpiCycle c
            WHERE c.isDeleted = false
              AND (LOWER(c.cycleCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(c.name)      LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<KpiCycle> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
