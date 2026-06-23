package vdt.kpimanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vdt.kpimanagement.entity.KpiTemplate;

import java.util.List;

public interface KpiTemplateRepo extends BaseRepository<KpiTemplate, Long> {
    List<KpiTemplate> findByIsActiveTrueAndIsDeletedFalse();
    List<KpiTemplate> findByCategory_IdAndIsActiveTrueAndIsDeletedFalse(Long categoryId);
    boolean existsByTemplateCodeAndIsDeletedFalse(String templateCode);

    @Query("""
            SELECT t FROM KpiTemplate t LEFT JOIN t.category c
            WHERE t.isDeleted = false
              AND (LOWER(t.templateCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(t.name)         LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(t.unit)         LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(c.name)         LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<KpiTemplate> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
