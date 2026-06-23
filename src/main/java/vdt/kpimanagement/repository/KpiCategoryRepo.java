package vdt.kpimanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vdt.kpimanagement.entity.KpiCategory;

public interface KpiCategoryRepo extends BaseRepository<KpiCategory, Long> {
    boolean existsByCategoryCodeAndIsDeletedFalse(String categoryCode);

    @Query("""
            SELECT c FROM KpiCategory c
            WHERE c.isDeleted = false
              AND (LOWER(c.categoryCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(c.name)         LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(c.description)  LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<KpiCategory> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
