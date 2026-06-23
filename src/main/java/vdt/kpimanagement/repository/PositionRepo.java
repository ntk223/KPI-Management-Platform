package vdt.kpimanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vdt.kpimanagement.entity.Position;

public interface PositionRepo extends BaseRepository<Position, Long> {
    boolean existsByPositionCodeAndIsDeletedFalse(String positionCode);

    @Query("""
            SELECT p FROM Position p
            WHERE p.isDeleted = false
              AND (LOWER(p.positionCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(p.title)        LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Position> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
