package vdt.kpimanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vdt.kpimanagement.entity.Department;

import java.util.List;

public interface DepartmentRepo extends JpaRepository<Department, Long> {
    List<Department> findByParentIsNullAndIsDeletedFalse();
    List<Department> findByParent_IdAndIsDeletedFalse(Long parentId);
    boolean existsByDepartmentCodeAndIsDeletedFalse(String departmentCode);
}
