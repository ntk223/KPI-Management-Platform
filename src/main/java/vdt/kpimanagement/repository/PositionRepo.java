package vdt.kpimanagement.repository;

import vdt.kpimanagement.entity.Position;

public interface PositionRepo extends BaseRepository<Position, Long> {
    boolean existsByPositionCodeAndIsDeletedFalse(String positionCode);
}
