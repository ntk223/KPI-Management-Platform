package vdt.kpimanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vdt.kpimanagement.entity.Department;

import java.util.List;

public interface DepartmentRepo extends BaseRepository<Department, Long> {
    List<Department> findByParentIsNullAndIsDeletedFalse();
    List<Department> findByParent_IdAndIsDeletedFalse(Long parentId);
    List<Department> findByManagerIdAndIsDeletedFalse(Long managerId);
    boolean existsByDepartmentCodeAndIsDeletedFalse(String departmentCode);

    @Query("""
            SELECT d FROM Department d
            WHERE d.isDeleted = false
              AND (LOWER(d.departmentCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(d.name)           LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Department> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
