package vdt.kpimanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vdt.kpimanagement.entity.KpiCategory;

import java.util.List;

public interface KpiCategoryRepo extends JpaRepository<KpiCategory, Long> {
    List<KpiCategory> findByIsDeletedFalse();
}
