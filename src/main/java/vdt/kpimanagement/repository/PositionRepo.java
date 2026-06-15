package vdt.kpimanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vdt.kpimanagement.entity.Position;

import java.util.List;

public interface PositionRepo extends JpaRepository<Position, Long> {
    List<Position> findByIsDeletedFalse();
    boolean existsByPositionCodeAndIsDeletedFalse(String positionCode);
}
