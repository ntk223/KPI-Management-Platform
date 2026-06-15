package vdt.kpimanagement.repository;

import vdt.kpimanagement.entity.KpiCategory;

public interface KpiCategoryRepo extends BaseRepository<KpiCategory, Long> {
    boolean existsByCategoryCodeAndIsDeletedFalse(String categoryCode);
}
