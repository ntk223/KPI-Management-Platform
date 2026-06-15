package vdt.kpimanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vdt.kpimanagement.entity.KpiCycle;
import vdt.kpimanagement.constant.enums.CycleStatus;

import java.util.List;
import java.util.Optional;

public interface KpiCycleRepo extends JpaRepository<KpiCycle, Long> {
    List<KpiCycle> findByIsDeletedFalse();
    List<KpiCycle> findByStatusAndIsDeletedFalse(CycleStatus status);
    Optional<KpiCycle> findByCycleCodeAndIsDeletedFalse(String cycleCode);
}
