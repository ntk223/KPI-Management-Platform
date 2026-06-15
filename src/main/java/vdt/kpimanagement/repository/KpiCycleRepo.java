package vdt.kpimanagement.repository;

import vdt.kpimanagement.entity.KpiCycle;
import vdt.kpimanagement.constant.enums.CycleStatus;

import java.util.List;
import java.util.Optional;

public interface KpiCycleRepo extends BaseRepository<KpiCycle, Long> {
    List<KpiCycle> findByStatusAndIsDeletedFalse(CycleStatus status);
    Optional<KpiCycle> findByCycleCodeAndIsDeletedFalse(String cycleCode);
}
