package vdt.kpimanagement.repository;

import vdt.kpimanagement.entity.Department;

import java.util.List;

public interface DepartmentRepo extends BaseRepository<Department, Long> {
    List<Department> findByParentIsNullAndIsDeletedFalse();
    List<Department> findByParent_IdAndIsDeletedFalse(Long parentId);
    boolean existsByDepartmentCodeAndIsDeletedFalse(String departmentCode);
}
